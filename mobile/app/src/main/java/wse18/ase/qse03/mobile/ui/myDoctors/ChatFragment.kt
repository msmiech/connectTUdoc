package wse18.ase.qse03.mobile.ui.myDoctors

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.bassaer.chatmessageview.model.Message
import com.github.bassaer.chatmessageview.view.ChatView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.ChatMessage
import wse18.ase.qse03.mobile.model.ChatThread
import wse18.ase.qse03.mobile.model.Office
import wse18.ase.qse03.mobile.model.Medicine
import wse18.ase.qse03.mobile.service.MedconectMessagingService
import wse18.ase.qse03.mobile.ui.MainActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.theartofdev.edmodo.cropper.CropImage
import wse18.ase.qse03.mobile.util.FileUtil
import wse18.ase.qse03.mobile.util.FirebaseUtil
import wse18.ase.qse03.mobile.util.PermissionUtil
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ChatFragment : Fragment() {

    private lateinit var viewModel: ChatViewModel
    private lateinit var mChatView: ChatView
    private lateinit var chatUserPatient: ChatUser
    private lateinit var chatUserOffice: ChatUser
    private var office: Office? = null
    private lateinit var broadCastReceiver: BroadcastReceiver
    private var chatThread: ChatThread? = null
    private var eThree: EThree? = null
    private var currentMessages: List<ChatMessage> = ArrayList()

    private var mCurrentPhotoPath: String = "" //Stores the path of the most recently taken photo
    private lateinit var mCurrentPhotoUri: Uri //Stores the Uri of the most recently taken photo
    private lateinit var cropImageDialogBuilder: AlertDialog.Builder

    private val FILE_CHOOSER_CODE: Int = 2
    private val TAKE_AND_SCAN_CODE: Int = 3
    private val CHOOSE_FILE_CODE: Int = 4
    private val TAKE_AND_SCAN_BARCODE: Int = 5

    private var barCodeDialog: AlertDialog? = null
    private var barCodeTextField: EditText? = null
    private var barcodeProcessing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            office = it.getSerializable(ARG_PARAM1) as Office?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get firebase user and set idToken later used for backend calls
        val user: FirebaseUser = FirebaseUtil.getFirebaseUser() ?: return view

        // Initalize chat participants
        val myId = 0
        val myIcon = BitmapFactory.decodeResource(resources, R.drawable.icon) //Icon will not be displayed
        val myName = if (user.displayName.isNullOrBlank()) getString(R.string.you) else user.displayName!!
        val yourId = 1
        val yourIcon = BitmapFactory.decodeResource(resources, R.drawable.icon) //Icon will not be displayed
        val yourName = if (office!!.name.isNullOrBlank()) getString(R.string.office) else office!!.name
        chatUserPatient = ChatUser(myId, myName, myIcon)
        chatUserOffice = ChatUser(yourId, yourName, yourIcon)

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.chat_fragment, container, false)

        mChatView = view.findViewById(R.id.chat_view)

        //Set UI parameters if you need
        mChatView.setRightBubbleColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        mChatView.setLeftBubbleColor(Color.WHITE)
        mChatView.setBackgroundColor(Color.LTGRAY)
        mChatView.setOptionButtonColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        mChatView.setSendButtonColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        mChatView.setSendIcon(R.drawable.ic_action_send)
        mChatView.setRightMessageTextColor(Color.WHITE)
        mChatView.setLeftMessageTextColor(Color.BLACK)
        mChatView.setUsernameTextColor(Color.DKGRAY)
        mChatView.setSendTimeTextColor(Color.DKGRAY)
        mChatView.setDateSeparatorColor(Color.DKGRAY)
        mChatView.setInputTextHint(getString(R.string.message_placeholder))
        mChatView.setMessageMarginTop(1)
        mChatView.setMessageMarginBottom(10)
        mChatView.setAutoHidingKeyboard(false)

        //Click Send Button
        mChatView.setOnClickSendButtonListener(View.OnClickListener {
            sendMessage(mChatView.inputText)
            mChatView.inputText = ""
        })

        mChatView.setOnClickOptionButtonListener(View.OnClickListener {
            Log.d(TAG, "Option button clicked")

            if (PermissionUtil.checkPermissions(context!!, PERMISSIONS)) {
                showAttachmentOptions(it)
            } else {
                Log.d(TAG, "Verify Permissions ...")
                requestPermissions(PERMISSIONS, PERMISSON_CODE)
            }
        })

        val builder = AlertDialog.Builder(context)
        val dialogView = layoutInflater.inflate(R.layout.send_text_message_dialog, null)
        builder.setView(dialogView)
        barCodeTextField = dialogView.findViewById(R.id.recognized_text_field)
        builder.setPositiveButton(R.string.btn_send) { dialog, id ->
            // User clicked OK button
            sendMessage(barCodeTextField!!.text.toString())
        }
        builder.setNegativeButton(R.string.btn_cancel)
        { dialog, id ->
            // User cancelled the dialog
        }

        builder.setMessage(R.string.send_message_question)?.setTitle(R.string.new_message)
        barCodeDialog = builder.create()


        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Get virgil security
        eThree = (activity as MainActivity).eThree

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.init()

        val messagesObserver = Observer<List<ChatMessage>> { messages ->
            showMessages(messages)
            currentMessages = messages
        }
        viewModel.messageListLD.observe(this, messagesObserver)


        val chatObserver = Observer<ChatThread> { chat ->
            FirebaseUtil.getFirebaseUser()!!.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {
                chatThread = chat
                val receiverUIDs = office!!.officeWorkers.map { w -> w.uid }
                viewModel.getMessagesByChatId(it.token, eThree!!, receiverUIDs, chatThread!!.id)
                //viewModel.startGettingMessagesByChatId(it.token, chat.id)
            })
        }
        viewModel.chatLD.observe(this, chatObserver)
        FirebaseUtil.getFirebaseUser()!!.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {
            viewModel.getChatByOfficeId(it.token, office!!.id)
        })
        registerBroadcastReceiver()

        cropImageDialogBuilder = AlertDialog.Builder(context)
        cropImageDialogBuilder.setMessage(R.string.select_part_for_text_recognition)
        cropImageDialogBuilder.setTitle(R.string.crop_image_question)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //viewModel.stopGettingMessages()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(broadCastReceiver)
    }

    private fun registerBroadcastReceiver() {
        broadCastReceiver = object : BroadcastReceiver() {
            override fun onReceive(contxt: Context?, intent: Intent?) {
                val officeToReload = intent?.getLongExtra("officeId", -1)
                if (officeToReload == office?.id) {
                    FirebaseUtil.getFirebaseUser()!!.getIdToken(false)
                        .addOnSuccessListener(OnSuccessListener<GetTokenResult> {
                            val receiverUIDs = office!!.officeWorkers.map { w -> w.uid }
                            viewModel.getMessagesByChatId(it.token, eThree!!, receiverUIDs, chatThread!!.id)
                        })
                }
            }
        }
        LocalBroadcastManager.getInstance(context!!)
            .registerReceiver((broadCastReceiver), IntentFilter(MedconectMessagingService.RELOAD_MESSAGES))
    }

    private fun showMessages(messages: List<ChatMessage>) {
        if (messages.isEmpty() || currentMessages.size - messages.size == 0) return

        val start = currentMessages.size
        val end = messages.size - 1
        for (i in start..end) {
            val m: ChatMessage = messages[i]
            val sentTime: Calendar = Calendar.getInstance()
            sentTime.time = m.createDateTime
            if (m.patientMessage) {
                val sentMessage = Message.Builder()
                    .setUser(chatUserPatient)
                    .setRight(true)
                    .setText(m.message)
                    .setSendTime(sentTime)
                    .hideIcon(true)
                    .build()
                mChatView.send(sentMessage)
            } else {
                val receivedMessage = Message.Builder()
                    .setUser(chatUserOffice)
                    .setRight(false)
                    .hideIcon(true)
                    .setText(m.message)
                    .setSendTime(sentTime)
                    .build()
                mChatView.receive(receivedMessage)
            }
        }
    }

    private fun sendMessage(message: String) {
        FirebaseUtil.getFirebaseUser()!!.getIdToken(false).addOnSuccessListener(OnSuccessListener<GetTokenResult> {

            val receiverUIDs = office!!.officeWorkers.map { w -> w.uid }
            viewModel.sendChatMessage(it.token, eThree!!, receiverUIDs, chatThread!!.id, message)
            viewModel.getMessagesByChatId(it.token, eThree!!, receiverUIDs, chatThread!!.id)
        })

    }


    fun showAttachmentOptions(view: View) {
        PopupMenu(context, view).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener { item: MenuItem ->
                when (item.itemId) {
                    R.id.take_photo -> {
                        if ((activity as MainActivity).checkPermissionsArray(PERMISSIONS)) {
                            openCamera(IMAGE_CAPTURE_CODE)
                        } else {
                            (activity as MainActivity).verifyPermissions(PERMISSIONS)
                            openCamera(IMAGE_CAPTURE_CODE)
                        }
                        true
                    }
                    R.id.send_file -> {
                        chooseAndSendFile()
                        true
                    }
                    R.id.scan_image -> {
                        performImageSearch()
                        true
                    }
                    R.id.take_and_scan_photo -> {
                        openCamera(TAKE_AND_SCAN_CODE)
                        true
                    }
                    R.id.send_file -> {
                        chooseAndSendFile()
                        true
                    }
                    R.id.take_and_scan_barcode -> {
                        openCamera(TAKE_AND_SCAN_BARCODE)
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.chat_attachments_menu)
            show()
        }

    }

    // Camera tools
    /**
     * Use this method to put the photo on the ChatView
     *
     * @param path - the original path to the picture to display
     */
    fun sendPhoto(path: String) {
        var picture = BitmapFactory.decodeFile(path)
        if (picture == null) {
            Log.d(TAG, "Sending of photo not possible - photo is null")
            return
        }
        val sendMessage = Message.Builder()
            .setUser(chatUserPatient)
            .setRight(true)
            .setPicture(picture)
            .hideIcon(true)
            .setType(Message.Type.PICTURE)
            .build()
        mChatView.send(sendMessage)
    }

    /**
     * Use this method to open the camera with the intent ACTION_IMAGE_CAPTURE
     */
    private fun openCamera(code: Int) {
        Log.d(TAG, "onClick: starting camera")
        /*
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        val imageUri = activity!!.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        // camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, code)
*/
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        context!!,
                        "com.example.android.fileprovider",
                        it
                    )
                    mCurrentPhotoUri = photoURI
                    takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, code)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            PERMISSON_CODE -> {
                if (grantResults.isNotEmpty() && grantResults.size == 2 && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) { //
                    // permission from popup was granted
                    //openCamera(IMAGE_CAPTURE_CODE)
                } else {
                    // permission from popup was denied
                    Toast.makeText(activity, "Berechtigungen wurden nicht gewährt.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // called when image was captured from camera intent
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == Activity.RESULT_OK) {
            //var photo = data?.extras?.get("data") as Bitmap -> ATTENTION: data is null here
            sendChatMessageWithFileAttachment("", mCurrentPhotoPath)
            sendPhoto(mCurrentPhotoPath)
        }

        if (requestCode == TAKE_AND_SCAN_CODE && resultCode == Activity.RESULT_OK) {
            cropImageDialogBuilder.setPositiveButton(R.string.yes) { dialog, id ->
                // User clicked OK button
                CropImage.activity(mCurrentPhotoUri).start(context!!, this)
            }
            cropImageDialogBuilder.setNegativeButton(R.string.btn_cancel)
            { dialog, id ->
                // User cancelled the dialog
                val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context!!, mCurrentPhotoUri)
                processImage(image)
            }
            cropImageDialogBuilder.create().show()
        }

        if (requestCode == TAKE_AND_SCAN_BARCODE && resultCode == Activity.RESULT_OK) {
            this.barcodeProcessing = true
            val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context!!, mCurrentPhotoUri)
            cropImageDialogBuilder.setPositiveButton(R.string.yes) { dialog, id ->
                // User clicked OK button
                CropImage.activity(mCurrentPhotoUri).start(context!!, this)
            }
            cropImageDialogBuilder.setNegativeButton(R.string.btn_cancel)
            { dialog, id ->
                // User cancelled the dialog
                val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context!!, mCurrentPhotoUri)
                processBarcode(image)
            }
            cropImageDialogBuilder.create().show()
        }

        if (requestCode == FILE_CHOOSER_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            data?.data?.also { uri ->
                Log.i(TAG, "Uri: $uri")
                cropImageDialogBuilder.setPositiveButton(R.string.yes) { dialog, id ->
                    // User clicked OK button
                    CropImage.activity(uri).start(context!!, this)
                }
                cropImageDialogBuilder.setNegativeButton(R.string.btn_cancel)
                { dialog, id ->
                    // User cancelled the dialog
                    val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context!!, uri)
                    processImage(image)
                }
                cropImageDialogBuilder.create().show()
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                val image: FirebaseVisionImage = FirebaseVisionImage.fromFilePath(context!!, resultUri)
                if (this.barcodeProcessing) {
                    processBarcode(image)
                    this.barcodeProcessing = false
                } else {
                    processImage(image)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                val toast = Toast.makeText(context, R.string.msg_error_while_cropping, Toast.LENGTH_LONG)
                toast.show()
            }
        }

        if (requestCode == CHOOSE_FILE_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            data?.data?.also { uri ->
                Log.i(TAG, "Sending chat message with attachment URI: $uri")
                var realPath = FileUtil.getRealPath(activity!!, uri)
                if (realPath == null) {
                    Log.wtf("onActivityResult::file", "Realpath of " + uri.toString() + " could not be determined!")
                    return
                }
                sendChatMessageWithFileAttachment("", realPath)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for/home/vanessa/Downloads/import_medicine.sql use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    private fun performImageSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "image/*"
        }

        startActivityForResult(intent, FILE_CHOOSER_CODE)
    }

    private fun chooseAndSendFile() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            addCategory(Intent.CATEGORY_OPENABLE)

            // Filter to show only images, using the image MIME data type.
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            type = "*/*"
        }

        startActivityForResult(intent, CHOOSE_FILE_CODE)
    }

    private fun sendChatMessageWithFileAttachment(message: String, realFilePath: String) {

        if (viewModel == null || chatThread == null) {
            return
        }
        if (realFilePath != null && !realFilePath.isEmpty()) {
            var file = File(realFilePath)
            if (file == null || !file.exists()) {
                Log.wtf("ChatFragment::msgAttach", "File does not exist!")
                return
            }
            var msgToSend = message
            if (msgToSend == null || msgToSend.isEmpty())
                msgToSend = "File sent: " + file.name
            FirebaseUtil.getFirebaseUser()!!.getIdToken(false).addOnSuccessListener {
                val receiverUIDs = office!!.officeWorkers.map { w -> w.uid }
                viewModel.sendChatMessageWithAttachment(
                    it.token,
                    eThree!!,
                    receiverUIDs,
                    chatThread!!.id,
                    msgToSend,
                    file
                )
                viewModel.getMessagesByChatId(it.token, eThree!!, receiverUIDs, chatThread!!.id)
            }
        }
    }

    private fun processBarcode(image: FirebaseVisionImage) {
        val detector = FirebaseVision.getInstance().visionBarcodeDetector
        val result = detector.detectInImage(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.size == 0) {
                    val toast = Toast.makeText(context, R.string.barcode_not_recognized, Toast.LENGTH_LONG)
                    toast.show()
                }
                for (barcode in barcodes) {
                    val rawValue = barcode.rawValue
                    if (barcode.rawValue == null || barcode.rawValue!!.length < 12) {
                        val toast = Toast.makeText(context, R.string.barcode_not_recognized, Toast.LENGTH_LONG)
                        toast.show()
                        continue
                    }

                    val pzn = barcode.rawValue!!.substring(6, 12)
                    // See API reference for complete list of supported types
                    FirebaseUtil.getFirebaseUser()!!.getIdToken(false)
                        .addOnSuccessListener(OnSuccessListener<GetTokenResult> {
                            viewModel.getMedicineFromBarcode(it.token, pzn)
                        })
                    val registerForMedicineObserver = Observer<Medicine> { medicine ->
                        if (medicine != null) {
                            barCodeTextField!!.setText(medicine.name)
                            if (!barCodeDialog!!.isShowing()) {
                                barCodeDialog?.show()
                            }
                        } else {
                            val toast = Toast.makeText(context, R.string.medicine_not_found, Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                    viewModel.medicine.observe(this, registerForMedicineObserver)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "processBarcode::failure")
                val toast = Toast.makeText(context, "Error: " + R.string.barcode_not_recognized, Toast.LENGTH_LONG)
                toast.show()
            }
    }

    private fun processImage(image: FirebaseVisionImage) {
        val detector = FirebaseVision.getInstance()
            .onDeviceTextRecognizer
        val result = detector.processImage(image)
            .addOnSuccessListener { firebaseVisionText ->
                // Task completed successfully
                Log.d(TAG, "CV::processImage::success: " + firebaseVisionText.text)
                val builder = AlertDialog.Builder(context)
                val dialogView = layoutInflater.inflate(R.layout.send_text_message_dialog, null)
                builder.setView(dialogView)
                val scannedTextField: EditText = dialogView.findViewById(R.id.recognized_text_field)
                if (firebaseVisionText.text.isNullOrEmpty()) {
                    val toast = Toast.makeText(context, R.string.text_not_recognized, Toast.LENGTH_LONG)
                    toast.show()
                } else {
                    scannedTextField.setText(firebaseVisionText.text)
                }

                builder.setPositiveButton(R.string.btn_send) { dialog, id ->
                    // User clicked OK button
                    sendMessage(scannedTextField.text.toString())
                }
                builder.setNegativeButton(R.string.btn_cancel)
                { dialog, id ->
                    // User cancelled the dialog
                }

                builder.setMessage(R.string.send_message_question)?.setTitle(R.string.new_message)
                val sendTextMessageDialog = builder.create()
                sendTextMessageDialog?.show()
            }
            .addOnFailureListener {
                // Task failed with an exception
                Log.d(TAG, "processImage::failure")
                val toast = Toast.makeText(context, "Error: " + R.string.text_not_recognized, Toast.LENGTH_LONG)
                toast.show()
            }

    }

    /**
     * Use this method to get the THUMBNAIL uri behind the image for future procedure
     *
     * @param inContext - the context of the fragment
     * @param inImage - the image to analyze
     */
    private fun getImageThumbnailUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream(100)
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    companion object {

        val TAG = "ChatFragment"

        const val ARG_PARAM1 = "office"

        // camera settings
        private val PERMISSON_CODE: Int = 1
        private val IMAGE_CAPTURE_CODE: Int = 1001
        var PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private val VERIFY_PERMISSIONS_REQUEST = 1

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 office, the office to chat with
         * @return A new instance of chat fragment.
         */
        @JvmStatic
        fun newInstance(param1: Long, param2: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}