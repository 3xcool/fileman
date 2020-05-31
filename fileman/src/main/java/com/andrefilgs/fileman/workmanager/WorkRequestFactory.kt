package com.andrefilgs.fileman.workmanager

import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import com.andrefilgs.fileman.auxiliar.*
import com.andrefilgs.fileman.auxiliar.FilemanInternalConstants
import com.andrefilgs.fileman.workmanager.workers.CoroutineFilemanWorker
import com.andrefilgs.fileman.workmanager.workers.FinalWorker

/**
 * I'm setting each tag as unique value
 */
class WorkRequestFactory {
  companion object{
    
    fun generateFilemanUniqueId(filenameFullPath:String, timestamp:Long ):String{
      return filenameFullPath + "_" + timestamp.toString()
    }
    
    /**
     * Just to flag to UI the end of work
     */
    fun buildFinalWorker(now:Long, cmd:String, filemanUniqueId:String, fileFullPath:String, fileContent:String?): OneTimeWorkRequest {
      val inputData = Data.Builder()
      // inputData.putBoolean(FilemanInternalConstants.WORK_KEY_IS_FINAL_WORKER, true)
      inputData.putInitTime(now)
      inputData.putFilemanCommand(cmd)
      inputData.putFilemanUniqueID(filemanUniqueId)
      inputData.putFileFullPath(fileFullPath)
      inputData.putFileContent(fileContent)
      return OneTimeWorkRequest.Builder(FinalWorker::class.java)
        .addTag(filemanUniqueId + FilemanInternalConstants.WORK_TAG_FINAL)
        .setInputData(inputData.build())
        .build()
    }
    
    fun buildFilemanWorkerRequest(
      command:String,
      now:Long,
      filemanUniqueId:String,
      tag:String? = FilemanInternalConstants.WORK_TAG_WRITE,  //Better use Fileman unique Id
      fileContent:String?,
      drive:Int,
      folder:String,
      filename: String,
      append:Boolean?,
      withTimeout:Boolean,
      timeout:Long
      )
      : OneTimeWorkRequest {
      
      val inputData = Data.Builder()
      inputData.putFilemanCommand(command)
      inputData.putInitTime(now)
      inputData.putFilemanUniqueID(filemanUniqueId)
      inputData.putFileContent(fileContent)
      inputData.putDrive(drive)
      inputData.putFolder(folder)
      inputData.putFilename(filename)
      inputData.putAppend(append)
      inputData.putWithTimeout(withTimeout)
      inputData.putTimeout(timeout)
      
      return OneTimeWorkRequest.Builder(CoroutineFilemanWorker::class.java)
        .addTag(tag!!)
        .setInputData(inputData.build())
        .build()
    }
  }
  
}