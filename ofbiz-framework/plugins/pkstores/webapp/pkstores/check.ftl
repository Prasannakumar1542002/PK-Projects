<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login From</title>
    <!-- <link rel="stylesheet" href="ok.css"> -->
    

</head>

<body>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            width: 100%;
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            letter-spacing: 1px;
            background-color: #0c1022;
        }

        .login_form_container {
            position: relative;
            width: 400px;
            height: 470px;
            max-width: 400px;
            max-height: 470px;
            background: #040717;
            border-radius: 50px 5px;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
            margin-top: 70px;
        }

        .login_form_container::before {

            position: absolute;
            width: 170%;
            height: 170%;
            content: '';
            background-image: conic-gradient(transparent, transparent, transparent, #ee00ff);
            animation: rotate_border 6s linear infinite;

        }

        .login_form_container::after {

            position: absolute;
            width: 170%;
            height: 170%;
            content: '';
            background-image: conic-gradient(transparent, transparent, transparent, #00ccff);
            animation: rotate_border 6s linear infinite;
            animation-delay: -3s;
        }

        @keyframes rotate_border {
            0% {
                transform: rotate(0deg);
            }

            100% {
                transform: rotate(360deg);
            }
        }

        .login_form {
            position: absolute;
            content: '';
            background-color: #0c1022;
            border-radius: 50px 5px;
            inset: 5px;
            padding: 50px 40px;
            z-index: 10;
            color: #00ccff;

        }

        h2 {
            font-size: 40px;
            font-weight: 600;
            text-align: center;
        }

        .input_group {
            margin-top: 40px;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: start;
        }

        .input_text {
            width: 95%;
            height: 30px;
            background: transparent;
            border: none;
            outline: none;
            border-bottom: 1px solid #00ccff;
            font-size: 20px;
            padding-left: 10px;
            color: #00ccff;

        }

        ::placeholder {
            font-size: 15px;
            color: #00ccff52;
            letter-spacing: 1px;

        }

        .fa {
            font-size: 20px;

        }

        #login_button {
            position: relative;
            width: 300px;
            height: 40px;
            transition: 1s;
            margin-top: 70px;


        }

        #login_button button {
            position: absolute;
            width: 100%;
            height: 100%;
            text-decoration: none;
            z-index: 10;
            cursor: pointer;
            font-size: 22px;
            letter-spacing: 2px;
            border: 1px solid transparent;
            border-radius: 50px;
            background-color: cyan;
            display: flex;
            justify-content: center;
            align-items: center;
            box-shadow: 0 0 5px cyan, 0 0 25px cyan;
            transition: 2s ease;
        }

        #login_button button:hover {
            box-shadow: 0 0 5px cyan, 0 0 25px cyan, 0 0 50px cyan, 0 0 100px cyan, 0 0 200px cyan;
            color: black;


        }

        .fotter {
            margin-top: 30px;
            display: flex;
            justify-content: space-between;

        }

        .fotter a {
            text-decoration: none;
            cursor: pointer;
            font-size: 18px;
            margin: 0.25rem 0;
            color: cyan;
        }

        .glowIcon {
            text-shadow: 0 0 10px #00ccff;

        }
    </style>
    <form id="myform" method="post">
        <input type="hidden" name="formid" value="login">
        <div class="login_form_container">
            <div class="login_form">
                <h2 id="head">${uiLabelMap.login}</h2>
                <div class="input_group">
                    <i class="fa fa-user"></i>
                    <input id="una" type="text" placeholder=${uiLabelMap.user} class="input_text" autocomplete="off"
                        name="user" required />
                </div>
                <div class="input_group">
                    <i class="fa fa-unlock-alt"></i>
                    <input id="pass" type="password" placeholder=${uiLabelMap.pass} class="input_text" autocomplete="off"
                        name="password" required />
                </div>
                <div class="button_group" id="login_button">
                    <button id="but">${uiLabelMap.login}</button>
                </div>
                <div class="fotter">
                    <a id="fp">${uiLabelMap.fp}</a>
                    <a href="register.html" id="sup">${uiLabelMap.sign}</a>
                </div>
            </div>
        </div>
    </form>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.1/jquery.min.js"></script>
    <script>
        $(".input_text").focus(function () {
            $(this).prev('.fa').addclass('glowIcon')
        })
        $(".input_text").focusout(function () {
            $(this).prev('.fa').removeclass('glowIcon')
        })

    </script>
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Get a reference to the form element
            const form = document.getElementById('myform');
            // Add a submit event listener to the form
            form.addEventListener('submit', function (event) {
                event.preventDefault(); // Prevent the default form submission
                // Get form data as a FormData object
                const formData1 = new FormData(form);
                const formData = new URLSearchParams();
                //formData.append('uname', 'ramu');
                //formData.append('upass', 'secret');
                for (const [field, value] of formData1) {
                    console.log(`${field}: ${value}`);
                    formData.append(field, value);
                }
                // Define the URL of the RESTful endpoint
                const apiUrl = '<@ofbizUrl>Validate</@ofbizUrl>'; // Replace with your API URL
                // Make a POST request to the API with the form data
                fetch(apiUrl, {
                    method: 'POST',
                    body: formData,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    mode: 'no-cors'
                })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json(); // Parse the response as JSON
                        
                    })
                    .then(data => {
                        // Handle the response data from the API
                        console.log('Response data:', data);
                       
                        // You can perform further processing here
                    })
                    .catch(error => {
                        // Handle any errors that occurred during the fetch
                        console.error('Fetch error:', error);
                    });
            });
        });
    </script>

</body>

</html>