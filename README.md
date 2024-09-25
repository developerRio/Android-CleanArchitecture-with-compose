## HomeScreen Composable
**Structure**
* Utilizes Scaffold for a structured layout, featuring:
* TopAppBar for the title and actions.
* FloatingActionButton for primary actions.
* Column for vertical arrangement of components.
* ModalBottomSheet for additional options.
**State Management**
* Employs LaunchedEffect to trigger network requests on first composition.
* Manages bottom sheet visibility using mutable state.
**Features**
* Collapsing App Bar: Displays hotel data that updates based on scroll position with CollapsingAppBarWithApi.
**Search Functionality**:
* Input field filters hotel results based on user input.
* Updates searchQuery in HomeViewModel dynamically.
**Vertical Hotel List:**
* Displays hotels in a LazyColumn, filtering results as per the search query.
* Uses HotelInfoItem for presenting individual hotel details.
**Error Handling:**
* Displays CircularProgressIndicator during loading and shows error messages for failed API calls.
## HomeViewModel
**Structure**
* Inherits from BaseViewModel and uses @HiltViewModel for dependency injection.
**State Management**
* Manages API response state using MutableStateFlow (hotelResponseStateFlow), initialized as ApiState.Empty.
* Maintains a mutable state for the current search query.
**Data Processing**
* Implements tabulatingAnalyticsForBottomSheet to analyze character occurrences in hotel names, returning the top three characters.
**Network Request**
* getAllHotelsNetworkRequest: Asynchronous method for fetching hotel data.
* Utilizes Dispatchers.IO for background execution.
* Handles exceptions with a catch block.
**Logging**
* Logs key events and states for debugging, including loading states and character occurrences.
**Overall Architecture**
**MVVM Pattern**: Separates UI logic from business logic.
**Dependency Injection**: Uses Hilt for modularity and testability.
**Jetpack Compose**: Leverages Jetpack Compose for a declarative UI, enhancing code readability and maintenance.
