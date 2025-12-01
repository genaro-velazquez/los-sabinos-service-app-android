package com.lossabinos.data.dto.valueobjects

import com.lossabinos.data.dto.entities.DTO
import com.lossabinos.data.dto.utilities.asInt
import com.lossabinos.domain.valueobjects.Pagination
import org.json.JSONObject

class PaginationDTO(
    val page:Int,
    val limit:Int,
    val totalPages:Int,
    val totalResults:Int
): DTO<Pagination>() {

    constructor(json: JSONObject) : this(
        page = json.asInt("page"),
        limit = json.asInt("limit"),
        totalPages = json.asInt("total_pages"),
        totalResults = json.asInt("total_results")
    )

    override fun toEntity(): Pagination = Pagination(
        page = page,
        limit = limit,
        totalPages = totalPages,
        totalResults = totalResults
    )
}