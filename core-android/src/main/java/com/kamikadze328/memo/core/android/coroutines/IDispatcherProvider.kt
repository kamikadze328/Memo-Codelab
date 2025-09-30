package com.kamikadze328.memo.core.android.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides the coroutines application scope and factory methods for creating local scopes used in activities, fragments, components etc.
 */
interface IDispatcherProvider {
    /**
     * Provides the IO coroutine dispatcher.
     */
    val io: CoroutineDispatcher

    /**
     * Provides the main coroutine dispatcher.
     */
    val main: CoroutineDispatcher

    /**
     * Provides the default coroutine dispatcher.
     */
    val default: CoroutineDispatcher

    /**
     * Provides the unconfined coroutine dispatcher.
     */
    val unconfined: CoroutineDispatcher
}