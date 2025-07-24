let currentQuizId = null;

      async function createQuiz() {
        const category = document.getElementById("category").value;
        const numQuestions = document.getElementById("numQuestions").value;
        const title = document.getElementById("title").value;
        const errorDiv = document.getElementById("error");
        const quizContainer = document.getElementById("quizContainer");
        const resultDiv = document.getElementById("result");

        // Clear previous quiz and results
        quizContainer.innerHTML = "";
        resultDiv.style.display = "none";
        errorDiv.style.display = "none";
        currentQuizId = null;

        if (!category || !numQuestions || !title) {
          errorDiv.textContent = "Please fill all fields.";
          errorDiv.style.display = "block";
          return;
        }

        try {
          const response = await fetch(
            `http://localhost:8080/quiz/create?category=${category}&numQ=${numQuestions}&title=${encodeURIComponent(
              title
            )}`,
            {
              method: "POST",
            }
          );
          if (!response.ok) throw new Error("Failed to create quiz");
          currentQuizId = await response.text(); // Directly use the quiz ID
          await loadQuizQuestions();
        } catch (error) {
          errorDiv.textContent = "Error creating quiz: " + error.message;
          errorDiv.style.display = "block";
        }
      }

      async function loadQuizQuestions() {
        if (!currentQuizId) return;

        try {
          const response = await fetch(
            `http://localhost:8080/quiz/get/${currentQuizId}`
          );
          if (!response.ok) throw new Error("Failed to load quiz");
          const questions = await response.json();
          const quizContainer = document.getElementById("quizContainer");

          quizContainer.innerHTML = questions
            .map(
              (q, index) => `
                    <div class="question">
                        <h3>${index + 1}. ${q.questionTitle}</h3>
                        <div class="options">
                            <label><input type="radio" name="q${q.id}" value="${
                q.option1
              }"> ${q.option1}</label>
                            <label><input type="radio" name="q${q.id}" value="${
                q.option2
              }"> ${q.option2}</label>
                            <label><input type="radio" name="q${q.id}" value="${
                q.option3
              }"> ${q.option3}</label>
                            <label><input type="radio" name="q${q.id}" value="${
                q.option4
              }"> ${q.option4}</label>
                        </div>
                    </div>
                `
            )
            .join("");

          quizContainer.innerHTML +=
            '<button onclick="submitQuiz()">Submit Quiz</button>';
        } catch (error) {
          document.getElementById("error").textContent =
            "Error loading quiz: " + error.message;
          document.getElementById("error").style.display = "block";
        }
      }

      async function submitQuiz() {
        if (!currentQuizId) return;

        const responses = [];
        const questions = document.querySelectorAll(".question");
        questions.forEach((q) => {
          const selected = q.querySelector(`input[type="radio"]:checked`);
          if (selected) {
            const questionId = q
              .querySelector('input[type="radio"]')
              .name.replace("q", "");
            responses.push({
              id: parseInt(questionId),
              response: selected.value,
            });
          }
        });

        if (responses.length !== questions.length) {
          document.getElementById("error").textContent =
            "Please answer all questions.";
          document.getElementById("error").style.display = "block";
          return;
        }

        try {
          const response = await fetch(
            `http://localhost:8080/quiz/submit/${currentQuizId}`,
            {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify(responses),
            }
          );
          if (!response.ok) throw new Error("Failed to submit quiz");
          const score = await response.json();
          const resultDiv = document.getElementById("result");
          resultDiv.textContent = `Your Score: ${score} / ${questions.length}`;
          resultDiv.style.display = "block";
        } catch (error) {
          document.getElementById("error").textContent =
            "Error submitting quiz: " + error.message;
          document.getElementById("error").style.display = "block";
        }
      }