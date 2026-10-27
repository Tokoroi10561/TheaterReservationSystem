document.addEventListener('click', function(event) {
    const addBtn = event.target.closest('.add-btn');
    const removeBtn = event.target.closest('.remove-btn');

    if(addBtn){
        const sectionType = addBtn.getAttribute('data-section');
        let containerId, rowClass, fieldName;

        if(sectionType === 'ticketType'){
            containerId = 'ticketType-container';
            rowClass = 'ticketType-row';
            fieldName = 'ticketTypes';
        } else if(sectionType === 'stage'){
            containerId = 'stage-container';
            rowClass = 'stage-row';
            fieldName = 'stages';
        } else if(sectionType === 'staff'){
            containerId = 'staff-container';
            rowClass = 'staff-row';
            fieldName = 'staffs';
        }

        const container = document.getElementById(containerId);
        const rows = container.querySelectorAll('.' + rowClass);
        const firstRow = rows[0];

        const newRow = firstRow.cloneNode(true);

        newRow.querySelectorAll('input').forEach(input => input.value = '');

        const btn = newRow.queryselector('button');
        btn.classList.remove('btn-outline-success', 'add-btn');
        btn.classList.add('btn-outline-danger', 'remove-btn');
        btn.removeAttribute('date-section');
        btn.innerHTML = '<i class="bi bi-dash-lg"></i> 削除';

        container.appendChild(newRow);

        reindexSection(containerId, rowClass, fieldName);
    }

    if(removeBtn){
        const row = removeBtn.closest('.ticket-row, .stage-row, .staff-row');
        const container = row.parentElement;
        const containerId = container.id;

        let rowClass, fieldName;
        if(containerId === 'ticket-container'){
            rowClass = 'ticket-row';
            fieldName = 'ticketTypes';
        } else if(containerId === 'stage-container'){
            rowClass = 'stage-row';
            fieldName = 'stages';
        } else if (containerId === 'staff-container'){
            rowClass = 'staff-row'
            fieldName = 'staffs';
        }

        row.remove();

        reindexSection(containerId, rowClass, fieldName);
    }
});

function reindexSection(containerId, rowClass, fieldName){
    const container = document.getElementById(containerId);
    const rows = container.querySelectorAll('.' + rowClass);

    rows.forEach((row, index) => {
        const inputs = row.querySelectorAll('input');
        inputs.forEach(input => {
            const nameAttr = input.getAttribute('name');
            if(nameAttr){
                const newName = nameAttr.replace(/\[\d+\]/, `[${index}]`);
                input.setAttribute('name', newName);
            }
        });
    });
}