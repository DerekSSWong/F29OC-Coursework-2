# F29OC-2020-21-CW2-Coursework: Specification

**Note that this Coursework is currently at draft stage**

### Introduction

*The full specification for CW2 is at:* `Vision > F29OC > Assessments > CW2`

You are required to write a `JobDispatcher` class and test it using *your own test code*.

This class will block *Storage* and *Compute* threads until there are enough of them to perform a specified *job*. 

Once the right combination of threads is available for a job, `JobDipatcher` will stop blocking the threads needed for the *job* and allow them to proceed.

Your class must implement the `Dispatcher` interface. You must not alter this interface. It contains three methods. Their behaviours are specified in the file `Dispatcher.java`.

### Instructions for Downloading CW2 Sub.

1. **Fork** this project to your own *private* namespace. **Do not change the name of this project**.
2. Check that the 'clone' url of your new project is ```git@gitlab-student.macs.hw.ac.uk:<your Vision/GitLab username>/f29oc-2019-20-cw2.git```.
3. Clone this project to a *private* local repository on your computer.
4. Edit `JobDispatcher.java` to develop your solution. Do not change the name of this class or the name of its method or return type - as this will cause the automatic making to fail and you will not receive any marks. Note that your `JobDispatcher` class must implement the supplied `Dispatcher` interface. Note also that you must not add any *thread safe* imports to `JobDispatcher.java` or `Dispatcher.java`.
5. Commit and push to *your* remote repository. Check in your browser that your remote repository  has been updated.
6. Extend the JUnit `Tests.java` to make sure that your `JobDispatcher` class fufils theCW2 requirements.
7. When you are ready to submit your project, download a zip of your code from your GitLab remote repository and submit the zip to *Vision > Assignments > CW2* by the CW2 deadline. The filename should be `<your Vision/GitLab username>.zip`.




















