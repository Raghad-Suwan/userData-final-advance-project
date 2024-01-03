# userData-project-


This is a report containing all the achieved functionalities as written in the requirements document along with the used design patterns. Each section contains the description of each feature, and what was done in order to achieve that.

1.Design Patterns
Strategy design pattern

We use a strategy pattern in the implemented( ItemDeletionHandler) interface  that defines the method handleDeletion  that encapsulates the different algorithms for deleting items. the concrete classes implement the interface to provide thor specific implementation for each one , the concrete classes ( PostDeletionHandler ,TransactionDeletionHandler
and ActivityDeletionHandler) classes . The (UserItemDeletion) class uses the strategy objects.It has a method deleteItems that accepts an ItemDeletionHandler parameter, which is used to delete items. and used in hard delete class

Proxy design pattern

These proxy classes act as a placeholder for the actual service classes(wrapper) , controlling access to the real service objects.allowing operations to be performed on the service before  interacting with the real service object. And that's to solve the problem of the Exception that caused by the Util class, which is SystemBusyException.
It is implemented in the UserServiceProxy, PostServiceProxy, PaymentProxy, and ActivityProxy classes.where every class implements the interface of the original class and modify the class that could cause an exception by replacing the util validateUserName with a ValidateUser function that we added.
The proxy classes could be used wherever we want to solve the exception(with get, delete, ….).

The factory design pattern

This strategy makes code simpler and more flexible. It is easy to add new types of file storage services in the future without changing the code(which achieve the OCP ).The Factory Method Pattern provides a high level of abstraction and encapsulation. The StorageFactory class serves as the factory, which is responsible for creating and returning instances of the IFileStorage interface.it creates an instance of either GoogleDrive or DropBox, or any service could be added.Every file storage service implements the IFilestorage interface (that have UploadFile function) to avoid concrete in the factory class by making an object of it for the services .The storageFactoy is used in the Downloader to upload the zipped file if Upload to file storage is selected.


2.Functional Requirements:
Export data
the user can export the data he choose from services that appears according to his type like( IAM , post , activity , payment )after he entered the UserName.Then after he choose the service there is a processUserChoices function that retrieve the data as a bytes of pdf files by the pdf converter (it get the data using the proxy and make a pdf files and add it to the pdfs list) convert the chosen data separately to pdf files using (apche.pdfbox)library. And then after getting the data as pdf files for downloading ,the pdf files of each service combined in a zip file by the The pdfZipper create zipped file function. And user choose the zip  file to be downloaded directly to the desktop , or to upload it to file storage service Google Drive and dropbox by the downloader class that handles different downloading options.


Delete data
there's two deletion process the user can do

hard delete
the user can delete his everything in the account by using deleteAcount function ,” there's no account any more “ (deletes the posts , payment and activities if exist, user profile with all data) by the handler and in this process we insure that the deleted user name never used again to another user by providing Map store the deleted user name , and any new user addition it checks if isDeleted

sofe deletion approach
you can delete everything except your email and user name

sure the deleted data depend on user user name , and type , such as the new user don't have activity , and if you not a premium user you don't have payment service



3.Non-Functional Requirements:

1. System scalability
   Each component has a clearly defined purpose, so we route the incoming requests to different components according to its type.so the workload is distributed uniformly.
   and design a parallel using (CompletableFuture) to perform asynchronous operations. This approach allows the system to handle multiple requests concurrently, thereby enhancing the system's speed and efficiency.peed .In addition to breaking the code into smaller and reusable modules and primitives that reduce the possibility of user pressure.

2. System accountability
   We used  org.slf4j:slf4j-api:1.7.30  to create debug and info logger in all classes for each action and store the logs in logger.log file . And maintain a clear and concise code

3. System flexibility
   The system is flexible by using interfaces and abstract classes , along with the application of design patterns so that it is flexible to changes. This makes it easier to modify or replace certain parts of the system without affecting others and achieve .  Adherence to the Open/Closed Principle enables the system to remain open for extension, yet closed for modification.And reduce concrete by the interfaces to make changes easiest . In addition to the use of encapsulation which hide complexities and dependencies for flexibility to change without effecting. And using the bytes in pdfs files that allows easier manipulation and storage of data in memory and provide flexibility in handling the content before saving.

4. System Availability
   We achieved the availability by  handling the exceptions and errors  with user-defined ones, so that when attempting to add a user whose username is already in use. This prevents the system from adding duplicate users.or when attempting to add a user who has already been deleted. This prevents the system from adding a user that doesn't exist anymore. Or when the user chooses the service to export multiple times , the exception prevents it . and there's a  BadRequestException, SystemBusyException,  NumberFormatException and NotFoundException. if any exception is thrown , then the error message printed .Our custom exception are :  UserAlreadyExistsException,UserDeletedException,  NumberFormatException and UnExpectedException .

5. System performance
   the well structured classes we create , applying design patterns  
   The code is simple and clean and easy to understand , we handle errors very well and the scalability is applied. The HardDelete class is designed to delete a user account and all associated data in parallel. Parallel processing indeed speeds up the deletion of items.This approach not only improves the system's performance but also increases its efficiency in handling large volumes of data. The same thing for the data export that is done as fast as it possible. 
