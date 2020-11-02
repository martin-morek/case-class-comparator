# Case class comparator

## Assigment 

- Given two instances of  the same Scala case class, write a method that takes two instances, compares them, and returns a data structure that "explains" their difference, in such a way, that it could be useful for debugging. This a problem not really solved, so try to give your best approximation. Come prepare to explain what the difficulties were and what trade-offs you made. 

- Assume there is a magic method like this one: `def deconstruct[A](a:A):YourGenericRepresentation = ???` This magic method will recursively deconstruct the case class, i.e. if there are cases class parameters it will in turn deconstruct them, and then return whatever `YourGenericRepresentation` is. You can take a look at this https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#generic-representation-of-sealed-families-of-case-classes to have an intuition on what "deconstructing" could mean. But remember this not your task, this would be given to you.

- Your task is:
    - Write an implementation for `def compare(a: YourGenericRepresentation, b: YourGenericRepresentation): AHelpfulRepresentation`
    - Explain how would you use `AHelpfulRepresentation` in real life, its main use case would be to compare two processes, that are only slightly different, and are consuming the exact same input, we want to explore the differences in their outputs
    - (NOTE: Please do not use those names for your types!)
    
## Notes 
 
`AHelpfulRepresentation` is represented as `Either[E, List[Difference]]`. Left side of either could contains unrecoverable errors, e.g. if comparing case classes are of different types. 
List in right side contains properties which are different if instances are equal the list is empty. 

`Difference` consists of three properties: 
   - `property` - name of argument which is different. It could contains whole path if case classes are nested.
   - `valueA` - value of property of first object 
   - `valueB` - value of property of second object 
   

### Solution restrictions:
- can compare only case classes which implements custom trait (`Decomposabe`). Only those case classes will be deconstructed, others will be consider as atomic elements.
