package com.androidtestapp.revolut.repository

interface Repository<T> {
    suspend fun invokeHeavyOperation(): T
}