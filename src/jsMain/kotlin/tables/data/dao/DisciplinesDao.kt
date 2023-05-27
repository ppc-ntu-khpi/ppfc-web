/*
 * Copyright (c) 2023. Vitalii Kozyr
 */

package tables.data.dao

import tables.data.model.DisciplineRequest
import tables.data.model.DisciplineResponse

interface DisciplinesDao {
    suspend fun saveDiscipline(disciplineRequest: DisciplineRequest)

    suspend fun updateDiscipline(disciplineRequest: DisciplineRequest, id: Long)

    suspend fun deleteDisciplines(ids: Set<Long>)

    suspend fun getDisciplines(
        limit: Long,
        offset: Long,
        searchQuery: String
    ): List<DisciplineResponse>
}