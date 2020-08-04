package com.hms.availabletoalllbraries.reflections

import android.content.Context
import android.util.Log
import java.lang.Exception
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

        fun getJavaClass(classPath: String): Class<*>
        {
            val cls = Class.forName(classPath)
            return cls
        }

        /**
         * Call companion function
         */
        fun callCompanionFunction(kotlinClass: KClass<*>, funcName: String):KFunction<*> {

            return kotlinClass.companionObject?.functions?.first { it.name == funcName } as KFunction<*>
        }

        /**
         * Call companion function
         */
        fun callCompanionJavaFunction(javaClass: Class<*>, funcName: String):Method {

            return javaClass.methods.first { it.name==funcName }
        }

        /**
         *
         */
        fun getCompanionObjectInstance(kotlinClass: KClass<*>) : Any?
        {
            return kotlinClass.companionObjectInstance
        }

        /**
         *
         */
        fun getJavaCompanionObjectInstance(kotlinClass: Class<*>) : Any?
        {
            return kotlinClass.newInstance()
        }

        /**
         *
         */
        fun getObjectInstance(kotlinClass: KClass<*>) : Any?
        {
            return kotlinClass.createInstance()
        }

        /**
         * Call normal function
         */
        fun  callFunction(kotlinClass: KClass<*>, funcName: String):KFunction<*> {
          //  val instance=kotlinClass.createInstance()
            return kotlinClass.declaredFunctions.first { it.name == funcName }
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
            try {
                var kotlinClass: KClass<*>? = null
                kotlinClass = CallClassMethods.getKotlinClass(pathName)
                callCompanionFunction(kotlinClass!!, methodName)
                    .call(getCompanionObjectInstance(kotlinClass!!), context)
            } catch (ex:Exception)
            {
                Log.e("ERROR",ex.message)
            }
        }

        fun moveToNewJavaActivity(pathName: String, methodName: String, context: Context)
        {
            try {
                var javaClass: Class<*>? = null
                javaClass = CallClassMethods.getJavaClass(pathName)
                javaClass.getDeclaredMethod(methodName,Context::class.java).invoke(null,context)
             //   callCompanionJavaFunction(javaClass!!, methodName).invoke(null, arrayOf(context))
            } catch (ex:Exception)
            {
                Log.e("ERROR",ex.message)
            }
        }


    }






}