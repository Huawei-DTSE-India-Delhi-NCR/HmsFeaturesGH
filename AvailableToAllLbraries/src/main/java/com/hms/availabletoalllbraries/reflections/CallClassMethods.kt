package com.hms.availabletoalllbraries.reflections

import android.content.Context
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.*

class CallClassMethods {

    companion object{

        fun getKotlinClass(classPath: String): KClass<*>
        {
            val cls = Class.forName(classPath)
            return cls.kotlin
        }

        /**
         * Call companion function
         */
        fun callCompanionFunction(kotlinClass: KClass<*>, funcName: String):KFunction<*> {

            return kotlinClass.companionObject?.functions?.first { it.name == funcName } as KFunction<*>
        }

        /**
         *
         */
        fun getCompanionObjectInstance(kotlinClass: KClass<*>) : Any?
        {
            return kotlinClass.companionObjectInstance
        }

        /**
         * Call normal function
         */
        fun callFunction(kotlinClass: KClass<*>, funcName: String):KFunction<*> {
          //  val instance=kotlinClass.createInstance()
            return kotlinClass.functions.first { it.name == funcName }
        }

        /**
         * Call normal function
         */
        fun callConstructorFunction(kotlinClass: KClass<*>):KFunction<*> {
            //  val instance=kotlinClass.createInstance()
            return kotlinClass.constructors.first()
        }



        fun moveToNewActivity(pathName: String, methodName: String, context: Context)
        {
            var kotlinClass: KClass<*>?=null
            kotlinClass=CallClassMethods.getKotlinClass(pathName)
            callCompanionFunction(kotlinClass!!,methodName)
                .call(getCompanionObjectInstance(kotlinClass!!), context)

        }



        }






}