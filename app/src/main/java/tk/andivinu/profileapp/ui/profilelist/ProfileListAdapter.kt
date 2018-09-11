package tk.andivinu.profileapp.ui.profilelist

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import tk.andivinu.profileapp.R
import tk.andivinu.profileapp.databinding.ItemProfileBinding
import tk.andivinu.profileapp.model.Profile

class ProfileListAdapter(val clickListener: (Profile) -> Unit) : RecyclerView.Adapter<ProfileListAdapter.ViewHolder>() {
    private lateinit var profileList: List<Profile>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileListAdapter.ViewHolder {
        val binding: ItemProfileBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_profile, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfileListAdapter.ViewHolder, position: Int) {
        holder.bind(profileList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return if (::profileList.isInitialized) profileList.size else 0
    }

    fun updateProfileList(profile: List<Profile>) {
        this.profileList = profile
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemProfileBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ProfileViewModel()

        fun bind(profile: Profile, clickListener: (Profile) -> Unit) {
            viewModel.bind(profile)
            binding.viewModel = viewModel
            binding.root.rootView.setOnClickListener { clickListener(profile) }
        }

    }
}