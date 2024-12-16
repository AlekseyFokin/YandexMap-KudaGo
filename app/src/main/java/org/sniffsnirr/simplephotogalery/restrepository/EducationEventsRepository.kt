package org.sniffsnirr.simplephotogalery.restrepository

import org.sniffsnirr.simplephotogalery.entities.EducationEvents

interface EducationEventsRepository {
    suspend fun getEducationEvents(): EducationEvents?
}