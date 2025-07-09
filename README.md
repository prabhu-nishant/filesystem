
1. # Create File: http://localhost:8080/file/create

    Request Body: New file at root level. You mention the path as '\\\\' if you are creating file at the root level </br>
    { </br>
      "fileType": "drive", </br>
      "name": "A", </br>
      "path": "\\\\" </br>  
    } </br>

    New file at another file. #Please don't mention \\ in the path at the end. This basically will create a file structure as A\\B
    {
      "fileType": "folder",
      "name": "B",
      "path": "A"   
    }

    New file at another file. #Please don't mention \\ in the path at the end. This basically will create a file structure as A\\B\\C
    {
      "fileType": "folder",
      "name": "C",
      "path": "A\\B"   
    }


2. # Delete file: http://localhost:8080/file/delete
    
    Request Body :  If you are deleting any file at root level. Please don't include \\ in the path at the end. This will delete A and all it's child files.
    {
      "path": "A"
    }

   If you are deleting any file apart from root level. Please don't include \\ in the path at the end. This will delete B and all it's child files.
   {
      "path": "A\\B"
   }
    
3. # Move a file: http://localhost:8080/file/move

   Request Body: Please don't include \\ in the path at the end. This will move file G and all its child files from C to B.
   {
     "sourcePath": "C\\G",
     "destinationPath": "B"
   }

4. # Write to file: http://localhost:8080/file/write-to-file

   Request Body : Please don't include \\ in the path at the end. This will overwrite the content to I text file
   {
       "path": "C\\G\\H\\I",
       "content": "ABC"
   }

5. # Print file content: http://localhost:8080/file/print-file-content

    Request Body : Please don't include \\ in the path at the end. This will print the content of I file
    {
        "path": "C\\G\\H\\I"
    }

6. # Print on IO : http://localhost:8080/file/print   

    Prints the file system on IO
