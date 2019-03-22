package wse18.ase.qse03.mobile.util

import android.util.Log
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import com.virgilsecurity.sdk.crypto.PublicKey
import java.util.Arrays.asList

object CryptoUtil {

    @JvmStatic fun encrypt(eThree: EThree, data: String, identities: List<String>, callback: (String) -> Unit) {
        // Listener for keys lookup
        val lookupKeysListener =
                object : EThree.OnResultListener<Map<String, PublicKey>> {
                    override fun onSuccess(result: Map<String, PublicKey>) {
                        Log.i("CryptoUtil", "Virgil security lookup public keys successful")
                        // Encrypt data using user public keys
                        val encryptedData = eThree.encrypt(data, result.values.toList())
                        Log.i("CryptoUtil", "Data encrypted")
                        callback(encryptedData)
                    }

                    override fun onError(throwable: Throwable) {
                        Log.wtf("CryptoUtil", "Error during virgil security public key lookup "+throwable.localizedMessage)
                    }
                }

        // Lookup destination user public keys
        eThree.lookupPublicKeys(identities, lookupKeysListener)
    }

    @JvmStatic fun decrypt(eThree: EThree, encryptedData: String, senderUid: String, callback: (String) -> Unit) {
        // Listener for keys lookup
        val lookupKeysListener =
                object : EThree.OnResultListener<Map<String, PublicKey>> {
                    override fun onSuccess(result: Map<String, PublicKey>) {
                        Log.i("CryptoUtil", "Virgil security lookup public keys successful")
                        // Encrypt data using user public keys
                        val data = eThree.decrypt(encryptedData, result[senderUid])
                        Log.i("CryptoUtil", "Data decrypted")
                        callback(data)
                    }

                    override fun onError(throwable: Throwable) {
                        Log.wtf("CryptoUtil", "Error during virgil security public key lookup "+throwable.localizedMessage)
                    }
                }

        // Lookup origin user public keys
        eThree.lookupPublicKeys(asList(senderUid),lookupKeysListener)
    }

}