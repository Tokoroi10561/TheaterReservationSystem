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

        // 入力値をクリア
        newRow.querySelectorAll('input').forEach(input => input.value = '');

        const btn = newRow.querySelector('button');
        btn.classList.remove('btn-outline-success', 'add-btn');
        btn.classList.add('btn-outline-danger', 'remove-btn');
        btn.removeAttribute('data-section');
        btn.innerHTML = '<i class="bi bi-dash-lg"></i> 削除';

        container.appendChild(newRow);

        reindexSection(containerId, rowClass, fieldName);
    }

    if(removeBtn){
        const row = removeBtn.closest('.ticketType-row, .stage-row, .staff-row');
        const container = row.parentElement;
        const containerId = container.id;

        let rowClass, fieldName;
        if(containerId === 'ticketType-container'){
            rowClass = 'ticketType-row';
            fieldName = 'ticketTypes';
        } else if(containerId === 'stage-container'){
            rowClass = 'stage-row';
            fieldName = 'stages';
        } else if (containerId === 'staff-container'){
            rowClass = 'staff-row';
            fieldName = 'staffs';
        }

        row.remove();

        reindexSection(containerId, rowClass, fieldName);
    }
});

// インデックス（0, 1, 2...）を name, id, for すべてキレイに振り直す関数
function reindexSection(containerId, rowClass, fieldName){
    const container = document.getElementById(containerId);
    const rows = container.querySelectorAll('.' + rowClass);

    rows.forEach((row, index) => {
        // 1. inputの name と id を書き換える
        const inputs = row.querySelectorAll('input');
        inputs.forEach(input => {
            const nameAttr = input.getAttribute('name');
            if(nameAttr){
                const newName = nameAttr.replace(/\[\d+\]/, `[${index}]`);
                input.setAttribute('name', newName);
            }
            const idAttr = input.getAttribute('id');
            if(idAttr){
                // 末尾の数字を新しいインデックスに置き換える（例: ticketName0 -> ticketName1）
                const newId = idAttr.replace(/\d+$/, index);
                input.setAttribute('id', newId);
            }
        });

        // 2. labelの for 属性も連番に書き換える
        const labels = row.querySelectorAll('label');
        labels.forEach(label => {
            const forAttr = label.getAttribute('for');
            if(forAttr){
                const newFor = forAttr.replace(/\d+$/, index);
                label.setAttribute('for', newFor);
            }
        });
    });
}