
export enum SessionStates {
    INITIAL,
    LOGGED_NEW_USER,    // A new user new logs in
    LOGGED_LAST_USER,   // The last user logs in
    LOGOUT,             // The user logs out
    DEACTIVATE,         // The user destroy enrollment
    THROW_OUT           // The refreshToken expires
}
