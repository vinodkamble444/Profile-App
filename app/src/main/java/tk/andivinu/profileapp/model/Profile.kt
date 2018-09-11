package tk.andivinu.profileapp.model




import android.arch.persistence.room.Embedded
import com.squareup.moshi.JsonClass
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Profile(
        @PrimaryKey
        var id: Int = 0,
        var profile_picture: String? ="",
        var first_name: String ="",
        var last_name: String ="",
        var isFavorite: Boolean =false
)
