const express = require('express')
const app = express()
const port = 3000

app.set('view engine', 'pug')

app.get('/login', (req, res) => {
   res.render('login', {})
})

app.get('/session', async (req, res) => {
   const response = await fetch("http://localhost:8080/session", {
     method: 'GET',
     credentials: 'include',
     headers: {
         'Content-Type': 'application/json',
         'Access-Control-Allow-Origin': 'localhost:8080',
     }
   })

   const data = await response.text()

   res.render('session', {"response": data})
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})
