$(document).ready(function() {
    console.log("✅ assignment.js loaded!");

    const csrfToken = $('meta[name="_csrf"]').attr('content');
    const csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // Toggle assignment status
    $('button').click(function() {
        const button = $(this);
        const assignmentId = button.data('assignment-id');
        const userId = $('#userId').val(); // optional hidden input with userId

        if (!assignmentId) {
            console.error("No assignment ID found on button!");
            return;
        }

        console.log("Sending toggle request for assignment:", assignmentId, "user:", userId);

        $.ajax({
            url: `/assignments/${userId}/toggle-ajax/${assignmentId}`,
            type: 'POST',
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function() {
                console.log("Toggle successful!");

                const taskLi = button.closest('li');

                if(button.hasClass('btn-success')) { // Pending -> Completed
                    button.removeClass('btn-success').addClass('btn-warning').text('Mark Pending');
                    $('#completed-tasks').append(taskLi);
                } else { // Completed -> Pending
                    button.removeClass('btn-warning').addClass('btn-success').text('Mark Complete');
                    $('#pending-tasks').append(taskLi);
                }
            },
            error: function(xhr, status, error) {
                console.error("Toggle failed!", xhr, status, error);
                alert('Failed to update assignment status!');
            }
        });
    });
});
