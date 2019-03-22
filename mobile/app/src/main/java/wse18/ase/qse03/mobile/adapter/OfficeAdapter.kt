package wse18.ase.qse03.mobile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item_office.view.*
import wse18.ase.qse03.mobile.R
import wse18.ase.qse03.mobile.model.Office

class OfficeAdapter(private val context: Context, private var dataSource: MutableList<Office>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.list_item_office, parent, false)
        val office = getItem(position) as Office
        rowView.office_name.text = office.name
        rowView.office_address.text = office.address.toString()
        return rowView
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return dataSource[position].id
    }


    fun getItemPosition(id: Long): Int{
        for(i in dataSource.indices){
            if(dataSource[i].id == id){
                return i
            }
        }
        return 0
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    fun updateData(list: List<Office>) {
        dataSource.clear()
        dataSource.addAll(list)
        this.notifyDataSetChanged()
    }


    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}