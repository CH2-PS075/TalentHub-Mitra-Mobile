package com.ch2ps075.talenthubmitra.data.network.api.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: Status
)

data class Status(

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("soil_types_prediction")
	val soilTypesPrediction: String,

	@field:SerializedName("confidence")
	val confidence: Any
)
