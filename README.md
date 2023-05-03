1. Start both the ktor backend (port 8080) and express frontend (port 3000)
2. Go to http://localhost:3000/login (what you put in the form doesn't matter)
3. Submission will POST to the backend to set the cookie, check storage for it
4. You'll be redirected to http://localhost:3000/session and it will say "Nope"
   which means it didn't find the cookie
