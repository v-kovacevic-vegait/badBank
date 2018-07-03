#  Simple banking simulator

Application is built using 2 classes. 

Account class models how much money a person has. Two main methods are
deposit and withdraw. Account may never have negative deposit. (There is an if statement preventing that).
Bank class models registering account and getting existing account (by id). 

Both classes have a lot of bugs.

# Simulation

There is a 3rd class BankRunner which has a main method.  
This class runs a simulation, where some number of accounts are created.
Then some random transfers are done. This is done on multiple threads.  
After that the overall amount of money on accounts should not change.

This seems to not be the case.

# Task

Your job is to find bugs and other problems in the implementation.
1. Make corrections to the Account and Bank class. (if You do not have time, please simply list problems that You see).
2. You may also have to change BankRunner class! In such case, please try to preserve its intended logic.
3. Additionally try to fix or list other potential code quality issues that You see. 

