package com.example.rss

data class StackoverflowData(val qus:String?, val ans :String?)
{
    override fun toString(): String = qus!!
}

