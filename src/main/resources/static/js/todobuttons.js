document.addEventListener("DOMContentLoaded", () => {
    const addAssignmentForm = document.getElementById("addAssignmentForm");
    const pendingTasks = document.getElementById("pending-tasks");
    const completedTasks = document.getElementById("completed-tasks");

    function getCsrfHeaders() {
        const token = document.querySelector('meta[name="_csrf"]').getAttribute("content");
        const header = document.querySelector('meta[name="_csrf_header"]').getAttribute("content");
        return { [header]: token };
    }

    if (addAssignmentForm) {
        addAssignmentForm.addEventListener("submit", async (e) => {
            e.preventDefault();

            const formData = new FormData(addAssignmentForm);
            const payload = {
                taskName: formData.get("taskName"),
                description: formData.get("description")
            };

            const response = await fetch("/assignments/ajax/add", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    ...getCsrfHeaders()
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const assignment = await response.json();

                const li = document.createElement("li");
                li.className = "list-group-item d-flex justify-content-between align-items-center";

                const divLeft = document.createElement("div");
                divLeft.className = "d-flex flex-column";

                const spanName = document.createElement("span");
                spanName.className = "fw-bold fs-5";
                spanName.textContent = assignment.task.taskName;

                const spanDesc = document.createElement("span");
                spanDesc.className = "text-muted small";
                spanDesc.textContent = assignment.task.description;

                divLeft.appendChild(spanName);
                divLeft.appendChild(spanDesc);

                const divRight = document.createElement("div");
                divRight.className = "d-flex gap-2";

                const completeBtn = document.createElement("button");
                completeBtn.className = "btn btn-sm btn-primary complete-btn";
                completeBtn.setAttribute("data-id", assignment.id);
                completeBtn.textContent = "Mark Complete";

                const deleteBtn = document.createElement("button");
                deleteBtn.className = "btn btn-sm btn-danger delete-btn";
                deleteBtn.setAttribute("data-id", assignment.id);
                deleteBtn.textContent = "Delete";

                divRight.appendChild(completeBtn);
                divRight.appendChild(deleteBtn);

                li.appendChild(divLeft);
                li.appendChild(divRight);

                pendingTasks.appendChild(li);

                addAssignmentForm.reset();
            } else {
                alert("Failed to add assignment");
            }
        });
    }

        function setupDeleteHandler(listElement) {
            listElement.addEventListener("click", async (e) => {
                if (e.target.classList.contains("delete-btn")) {
                    const assignmentId = e.target.getAttribute("data-id");

                    const response = await fetch(`/assignments/ajax/${assignmentId}/delete`, {
                        method: "DELETE",
                        headers: getCsrfHeaders()
                    });

                    if (response.ok) {
                        e.target.closest("li").remove();
                    } else {
                        alert("Failed to delete assignment");
                    }
                }
            });
        }

        setupDeleteHandler(pendingTasks);
        setupDeleteHandler(completedTasks);

    function setupToggleHandler(listElement) {
        listElement.addEventListener("click", async (e) => {
            if (e.target.classList.contains("complete-btn") ||
                e.target.classList.contains("pending-btn")) {

                const assignmentId = e.target.getAttribute("data-id");
                const response = await fetch(`/assignments/ajax/${assignmentId}/toggle`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        ...getCsrfHeaders()
                    }
                });

                if (response.ok) {
                    const assignmentElement = e.target.closest("li");
                    assignmentElement.remove();

                    if (e.target.classList.contains("complete-btn")) {
                        e.target.outerHTML = `<button class="btn btn-sm btn-warning pending-btn" data-id="${assignmentId}">Mark Pending</button>`;
                        completedTasks.appendChild(assignmentElement);
                    } else {
                        e.target.outerHTML = `<button class="btn btn-sm btn-primary complete-btn" data-id="${assignmentId}">Mark Complete</button>`;
                        pendingTasks.appendChild(assignmentElement);
                    }
                } else {
                    alert("Failed to toggle status");
                }
            }
        });
    }

    setupToggleHandler(pendingTasks);
    setupToggleHandler(completedTasks);
});
