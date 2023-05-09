# Fix and refactor

##  Simple bank 

The application is built using 2 classes. 

Account class models how much money a person has. Two main methods are
deposit and withdraw. Account may never have negative deposit. (There is an if statement preventing that).
Bank class models registering account and getting existing account (by id). 

Both classes have a lot of bugs.

## Simulation

There is a 3rd class BankRunner which has a main method.  
This class runs a simulation, where some number of accounts is created.
Then some random transfers are done. This is performed using multiple threads.  
After that the overall amount of money on accounts should not change.

This seems not to be the case.

##  Task

Your job is to find bugs and other problems in the implementation.
1. Make corrections to the Account and Bank classes. (if you do not have time, please simply list the problems that you see).
2. You may also have to change BankRunner class! In such case, please try to preserve its intended logic.
3. Additionally try to fix or list other potential code quality issues that you see.
4. You are free to create additional classes or files as needed. 


##  Improvements
Bugs found and solved:
- The Account object is accessed by multiple threads that might withdraw or deposit money to it, which can lead to inconsistent state.
To solve this problem, keyword 'synchronized' is added on corresponding methods. By synchronizing 
these methods, threads are forced to acquire a lock on the object before they can execute their code, which guarantees that only one thread 
can execute these methods at a time. This ensures that the account balance is always updated correctly and consistently.
- Deposit action is executed before withdraw action, which can lead to inconsistent state when withdraw action is disabled 
and exception is thrown due to account not having enough money. To solve this problem, order of these methods is switched. 
In case of introducing database, these two methods should be considered as one transaction, therefore if either of them fails,
a rollback should be executed.


Code and syntax improvements:
- The code is refined by replacing print statements with a logger to improve the handling of messages.
- String formatting is used instead of concatenation to ensure code clarity and maintainability.
- Constants are used to represent log messages, error messages, and default values to improve code readability.
- To minimize the occurrence of calculations that are performed multiple times, such as multiplying the number of accounts and default deposit, these computations have been separated to execute only once.
- Smaller methods have been implemented to handle atomic operations such as transferring money, generating BigDecimal values, and validating input values to improve code modularity and reusability.
- Map interface is used instead of HashMap implementation to make code depend on abstractions and improve flexibility, according to dependency inversion principle.
- A check is added to determine if an account exists, and if not, a NoSuchElementException is thrown.
- Unit tests are included to verify the accuracy and functionality of methods used for getting and registering accounts, as well as withdrawing and depositing money.


Domain improvements:
- Money value is represented by BigDecimal, which allows for better precision, a crucial factor when dealing with money.
- Negative money value now results in an exception being thrown, enforcing the restriction that money cannot be negative.


Other ideas:
- Money could be represented as a new class whose attribute stores the value. Having money as a type can be useful when introducing
different currencies. For the sake of simplicity, in this case there is no new class for Money because it is implied
that currency is always the same.(https://www.martinfowler.com/eaaCatalog/money.html)
- By encapsulating money class, operations such as adding and subtracting money can be moved to the Money class, following the single responsibility principle.
- As an additional improvement, custom exceptions with have pre-defined and formatted messages could be created, so they would not be handled as part of the business logic.


Below is a sequence diagram that represents one random money transfer operation:

![Bad_Bank_Random_Operation_Sequence_Diagram](https://user-images.githubusercontent.com/104908897/236853704-7e98f1c5-733d-46e7-9af4-aafd4f533015.png)
