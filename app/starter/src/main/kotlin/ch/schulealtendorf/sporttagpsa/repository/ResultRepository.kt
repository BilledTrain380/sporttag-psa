package ch.schulealtendorf.sporttagpsa.repository

import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import org.springframework.data.repository.CrudRepository

interface ResultRepository : CrudRepository<ResultEntity, Int>