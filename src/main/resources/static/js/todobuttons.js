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
            li.setAttribute("data-id", assignment.id);
            li.innerHTML = `
                <div class="d-flex flex-column">
                    <span class="fw-bold fs-5">${assignment.task.taskName}</span>
                    <span class="text-muted small">${assignment.task.description}</span>
                </div>
                <div class="btn-group">
                    <button class="btn btn-sm btn-primary complete-btn" data-id="${assignment.id}">Mark Complete</button>
                    <button class="btn btn-sm btn-danger delete-btn" data-id="${assignment.id}">Delete</button>
                </div>
            `;
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
