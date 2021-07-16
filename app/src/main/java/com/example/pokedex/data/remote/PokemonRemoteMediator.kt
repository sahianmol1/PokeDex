package com.example.pokedex.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.pokedex.api.PokeApi
import com.example.pokedex.api.PokeApi.Companion.PAGING_SIZE
import com.example.pokedex.data.local.PokemonDatabase
import com.example.pokedex.models.Pokemon
import com.example.pokedex.models.PokemonKeys
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

const val MPOKEMON_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
class PokemonRemoteMediator(private val pokeApi: PokeApi, private val database: PokemonDatabase) :
        RemoteMediator<Int, Pokemon>() {

    override suspend fun load(
            loadType: LoadType,
            state: PagingState<Int, Pokemon>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> POKEMON_STARTING_PAGE_INDEX
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                if (remoteKey?.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                remoteKey.nextKey
            }
        }

//        val loadKey: PokemonKeys? = when (loadType) {
//            LoadType.REFRESH -> null
//            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            LoadType.APPEND -> {
//                state.lastItemOrNull()
//                        ?: return MediatorResult.Success(endOfPaginationReached = true)
//                getPokeKeys()
//            }
//        }


        return try {

//            val myPrevKey: Int? = when(loadKey?.prevKey == null) {
//                true ->  0
//                else -> loadKey?.prevKey
//            }
//
//            val page = loadKey?.nextKey ?: POKEMON_STARTING_PAGE_INDEX

            val response = pokeApi.getAllPokemon(offsetSize = page?.times(PAGING_SIZE))
            val pokemons = response?.results
            val endOfPaginationReached = pokemons.isEmpty()
            if (pokemons != null) {
                database.withTransaction {
//                    if (loadType == LoadType.REFRESH) {
//                        database.getKeysDao().clearRemoteKeys()
//                        database.getPokemonDao().clearPokemons()
//                    }

                    val prevKey = if (page == POKEMON_STARTING_PAGE_INDEX) null else page?.minus(1)
                    val nextKey = if (endOfPaginationReached) null else page?.plus(1)
                    val keys = pokemons.map {
                        PokemonKeys(
                                prevKey = prevKey,
                                nextKey = nextKey
                        )
                    }


                    database.getKeysDao().saveKeys(keys)
                    database.getPokemonDao().saveAllPokemon(pokemons)
                }

            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getPokeKeys(): PokemonKeys? {
        return database.getKeysDao().getAllKeys().lastOrNull()
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Pokemon>): PokemonKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
                ?.let { pokemonKey ->
                    // Get the remote keys of the last item retrieved
                    database.getKeysDao().getKeysByPokemonId(pokemonKey.id)
                }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Pokemon>): PokemonKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
                ?.let { pokemonKey ->
                    // Get the remote keys of the first items retrieved
                    database.getKeysDao().getKeysByPokemonId(pokemonKey.id)
                }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
            state: PagingState<Int, Pokemon>
    ): PokemonKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { pokemonId ->
                database.getKeysDao().getKeysByPokemonId(pokemonId)
            }
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

}