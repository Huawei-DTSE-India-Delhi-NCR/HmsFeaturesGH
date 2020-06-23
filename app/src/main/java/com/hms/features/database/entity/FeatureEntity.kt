package com.hms.features.database.entity

import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hms.features.view.FeatureDetailsActivity
import java.util.*

@Entity(tableName = "features")
class FeatureEntity() : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "feature_id")
    var featureID: Long? = null
    @ColumnInfo(name = "feature_name")
    var featureName: String? = null
    @ColumnInfo(name = "description")
    var featureDescription: String? = null
    @ColumnInfo(name = "details")
    var featureDetails: String? = null
    @ColumnInfo(name = "type")
    var featureType: String? = null


    constructor(featureID: Long?,  featureName: String?, featureType: String?, featureDescription: String?, featureDetails: String?) : this() {
        this.featureID = featureID
        this.featureName = featureName
        this.featureDescription = featureDescription
        this.featureDetails = featureDetails
        this.featureType=featureType
    }



    constructor(parcel: Parcel) : this() {
        featureID = parcel.readValue(Long::class.java.classLoader) as? Long
        featureName = parcel.readString()
        featureDescription =parcel.readString()
        featureDetails = parcel.readString()
        featureType = parcel.readString()
    }

    // for DiffUtil class
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FeatureEntity) return false
        val featureEntity = other as? FeatureEntity
        return featureID == featureEntity?.featureID
                && featureName == featureEntity?.featureName
                && featureDescription == featureEntity?.featureDescription
                && featureDetails == featureEntity?.featureDetails
                && featureType== featureEntity?.featureType
    }

    override fun hashCode(): Int {
        return Objects.hash(featureID, featureName, featureDescription, featureDetails)
    }

    // dataBinding onClick
    fun onFeatureItemClicked(view: View, feature: FeatureEntity) {

        FeatureDetailsActivity.newIntent(context = view.context,featureEn = feature)
    }

    // parcelable stuff
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(featureID)
        parcel.writeString(featureName)
        parcel.writeString(featureDescription)
        parcel.writeString(featureDetails)
        parcel.writeString(featureType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FeatureEntity> {
        override fun createFromParcel(parcel: Parcel): FeatureEntity {
            return FeatureEntity(parcel)
        }

        override fun newArray(size: Int): Array<FeatureEntity?> {
            return arrayOfNulls(size)
        }
    }
}