
1. # Create File: http://localhost:8080/file/create

    Request Body: New file at root level. You mention the path as '\\\\' if you are creating file at the root level </br>
    { </br>
      "fileType": "drive", </br>
      "name": "A", </br>
      "path": "\\\\" </br>  
    } </br>

    New file at another file. #Please don't mention \\\\ in the path at the end. This basically will create a file structure as A\\\\B </br>  
    { </br> 
      "fileType": "folder", </br> 
      "name": "B", </br> 
      "path": "A"  </br> 
    } </br> 

    New file at another file. Please don't mention \\\\ in the path at the end. This basically will create a file structure as A\\\\B\\\\C  </br>  
    { </br> 
      "fileType": "folder", </br> 
      "name": "C", </br> 
      "path": "A\\\\B" </br> 
    } </br> 


2. # Delete file: http://localhost:8080/file/delete
    
    Request Body :  If you are deleting any file at root level. Please don't include \\\\ in the path at the end. This will delete A and all it's child files. </br> 
    { </br> 
      "path": "A" </br> 
    } </br> 

   If you are deleting any file apart from root level. Please don't include \\\\ in the path at the end. This will delete B and all it's child files. </br> 
   { </br> 
      "path": "A\\\\B" </br> 
   } </br> 
    
3. # Move a file: http://localhost:8080/file/move

   Request Body: Please don't include \\\\ in the path at the end. This will move file G and all its child files from C to B. </br> 
   { </br> 
     "sourcePath": "C\\\\G", </br> 
     "destinationPath": "B" </br> 
   } </br> 

4. # Write to file: http://localhost:8080/file/write-to-file

   Request Body : Please don't include \\\\ in the path at the end. This will overwrite the content to I text file </br> 
   { </br> 
       "path": "C\\\\G\\\\H\\\\I", </br> 
       "content": "ABC" </br> 
   } </br> 

5. # Print file content: http://localhost:8080/file/print-file-content

    Request Body : Please don't include \\\\ in the path at the end. This will print the content of I file </br> 
    { </br> 
        "path": "C\\\\G\\\\H\\\\I" </br> 
    } </br> 

6. # Print on IO : http://localhost:8080/file/print   

    Prints the file system on IO 
