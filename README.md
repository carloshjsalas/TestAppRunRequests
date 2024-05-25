# Test App for Run Request
Sample application that defines and runs 2 requests simultaneously, each request is defined below:

### 1. Every10thCharacterRequest:
- **a**. Grab https://www.compass.com/about/ content from the web.
- **b.** Find every 10th character (i.e. 10th, 20th, 30th, etc.) and display the array on the screen.

### 2. WordCounterRequest:
- **a**. Grab https://www.compass.com/about/ content from the web.
- **b**. Split the text into words using whitespace characters (i.e. space, tab, line break, etc.), count the occurrence of every unique word (case insensitive) and display the count for each word on the
screen.

### 3. Considerations:
- Consider the content plain-text, regardless of what is returned by the response.
- Treat anything separated by whitespace characters as a single word.
- The data is cached and available offline after the first fetch while the app is open.
- Main methods from DetailsViewModel where unit-tested.

## Architecture MVVM

### Technologies used

Kotlin Android Native, MVVM, DagerHilt, Navigation Component, Material Component 3, Coroutines, Kotlin Flow, LiveData, ViewModel, JUnit, Mockito.

![TestApp](https://github.com/carloshjsalas/TestAppRunRequests/assets/8594582/c39eac82-0772-48b6-b13d-bf409b6b2dbd)
