package com.adrianbadarau.config

enum class Messages (val text:String){
    SUCCESS("Operation completed successfully "),
    ERROR("Sorry but something went wrong"),
    NO_STOCK_ERROR("Sorry but the product you wanted to buy is no longer in stock, please try again later"),
    NOT_FOUND ("It seems that we don't have this item anymore"),
}