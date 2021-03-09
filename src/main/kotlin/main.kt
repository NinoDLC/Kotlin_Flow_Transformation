import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    val start = System.currentTimeMillis()

    println("Start !")

    val propertiesFlow = getProperties()

    runBlocking {
        propertiesFlow.transform { properties ->
            properties.forEach { property ->
                getPhotos(property.propertyId).collect { photos ->
                    emit(
                        Wrapper(
                            property,
                            photos
                        )
                    )
                }
            }
        }.collect {
            println("Collect called at ${System.currentTimeMillis() - start} with value = $it")
        }
    }
}

data class Wrapper(
    val property: Property,
    val photos: List<Photo>
)

fun getProperties(): Flow<List<Property>> = flowOf(
    listOf(
        Property(propertyId = 1),
        Property(propertyId = 2)
    ),
    listOf(
        Property(propertyId = 1),
        Property(propertyId = 2),
        Property(propertyId = 3)
    )
).onEach {
    delay(2_000)
}

fun getPhotos(propertyId: Int): Flow<List<Photo>> = flowOf(
    List(3) {
        Photo(
            photoUrl = "photoUrl$it",
            propertyId = propertyId
        )
    }
).onEach {
    delay(1_000)
}

data class Property(
    val propertyId: Int
)

data class Photo(
    val photoUrl: String,
    val propertyId: Int
)