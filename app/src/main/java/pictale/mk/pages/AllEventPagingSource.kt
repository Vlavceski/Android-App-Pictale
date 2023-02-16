package pictale.mk.pages

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class AllEventPagingSource(
private val apiService: ApiService,
private val eventPublicityType: String?
) : PagingSource<Int, ResponsePages>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResponsePages> {
        val page = params.key ?: 0
        val pageSize = params.loadSize

        return try {
            val response = apiService.getPages(
                eventPublicityType = eventPublicityType,
                page = page.toString(),
                size = pageSize.toString()
            )

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val eventList = body.content
                    val totalPages = body.totalPages

                    LoadResult.Page(
                        data = eventList,
                        prevKey = if (page == 0) null else page - 1,
                        nextKey = if (page == totalPages - 1) null else page + 1
                    )
                } else {
                    LoadResult.Error(IllegalStateException("Response body is null"))
                }
            } else {
                LoadResult.Error(HttpException(response))
            }
        }
        catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ResponsePages>): Int? {
        return null
    }


}

