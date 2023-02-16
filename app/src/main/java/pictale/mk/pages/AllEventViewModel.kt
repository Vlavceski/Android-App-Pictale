package pictale.mk.pages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel()
class AllEventViewModel
@Inject constructor(private val apiService: ApiService): ViewModel(){
    val listData= Pager(PagingConfig(pageSize=1)){
        AllEventPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)
}
