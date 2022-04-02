package sk.kasper.domain.usecase

import sk.kasper.domain.model.Photo
import sk.kasper.domain.model.Response

fun interface GetPhotos {
    suspend operator fun invoke(launchId: String): Response<List<Photo>>
}