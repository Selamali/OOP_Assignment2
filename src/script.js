const API_URL = "/questions";
const API_CANDIDATE_URL = "/candidates";

let questionsData = [];
let candidatesData = [];
let currentSort = { field: null, dir: null };
let currentCandSort = { field: null, dir: null };

let editModal, deleteModal, editCandidateModal, deleteCandidateModal, successToast, errorToast;

document.addEventListener('DOMContentLoaded', () => {
    editModal = new bootstrap.Modal(document.getElementById('editModal'));
    deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
    editCandidateModal = new bootstrap.Modal(document.getElementById('editCandidateModal'));
    deleteCandidateModal = new bootstrap.Modal(document.getElementById('deleteCandidateModal'));

    successToast = new bootstrap.Toast(document.getElementById('successToast'));
    errorToast = new bootstrap.Toast(document.getElementById('errorToast'));

    const searchInput = document.getElementById('searchInput');
    if (searchInput) searchInput.addEventListener('input', debounce(() => { loadQuestions(); }, 300));

    const searchCandInput = document.getElementById('searchCandidateInput');
    if (searchCandInput) searchCandInput.addEventListener('input', debounce(() => { loadCandidates(); }, 300));

    document.getElementById('addForm')?.addEventListener('submit', handleAdd);
    document.getElementById('addCandidateForm')?.addEventListener('submit', handleAddCandidate);

    loadQuestions();
});

// --- QUESTIONS LOGIC ---
async function loadQuestions() {
    try {
        const response = await fetch(API_URL);
        questionsData = await response.json();
        renderQuestions(questionsData);
    } catch (e) { console.error("Load Q error", e); }
}

function renderQuestions(data) {
    const tbody = document.getElementById("tableBody");
    const query = document.getElementById("searchInput")?.value.toLowerCase() || "";
    tbody.innerHTML = "";

    const totalPoints = data.reduce((sum, q) => sum + (q.marks || 0), 0);
    const avg = data.length ? (totalPoints / data.length).toFixed(1) : 0;
    document.getElementById("countBadge").textContent = data.length;
    document.getElementById("totalPoints").textContent = totalPoints;
    document.getElementById("avgScore").textContent = avg;

    let filtered = data.filter(q => (q.text || "").toLowerCase().includes(query));

    if (currentSort.dir) {
        filtered.sort((a, b) => {
            let valA = a[currentSort.field];
            let valB = b[currentSort.field];
            if (typeof valA === 'string') { valA = valA.toLowerCase(); valB = valB.toLowerCase(); }
            if (valA < valB) return currentSort.dir === 'asc' ? -1 : 1;
            if (valA > valB) return currentSort.dir === 'asc' ? 1 : -1;
            return 0;
        });
    } else {
        filtered.sort((a, b) => a.id - b.id);
    }

    updateSortIcons(currentSort, ['category', 'text', 'marks']);

    filtered.forEach(q => {
        const tr = document.createElement('tr');
        let badgeClass = 'badge-pastel-general';
        if(q.category === 'Java') badgeClass = 'badge-pastel-java';
        if(q.category === 'SQL') badgeClass = 'badge-pastel-sql';
        if(q.category === 'Soft Skills') badgeClass = 'badge-pastel-soft';
        if(q.category === 'Algorithms') badgeClass = 'badge-pastel-algo';

        const badgeHtml = `<span class="badge ${badgeClass} badge-pastel-text">${q.category || 'General'}</span>`;

        tr.innerHTML = `
            <td class="ps-4">${badgeHtml}</td>
            <td class="wrap-text">${q.text}</td>
            <td class="text-center"><span class="badge-points">${q.marks}</span></td>
            <td class="text-end pe-4">
                <button class="btn btn-icon btn-edit" onclick="openEditModal(${q.id})"><i class="bi bi-pencil-fill"></i></button>
                <button class="btn btn-icon btn-delete" onclick="openDeleteModal(${q.id})"><i class="bi bi-trash-fill"></i></button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

// --- CANDIDATES LOGIC ---
async function loadCandidates() {
    try {
        const response = await fetch(API_CANDIDATE_URL);
        candidatesData = await response.json();
        renderCandidates(candidatesData);
    } catch (e) { console.error(e); }
}

function renderCandidates(data) {
    const tbody = document.getElementById("candidatesTableBody");
    const query = document.getElementById("searchCandidateInput")?.value.toLowerCase() || "";
    tbody.innerHTML = "";

    let filtered = data.filter(c => (c.name || "").toLowerCase().includes(query));

    if (currentCandSort.dir) {
        filtered.sort((a, b) => {
            let valA = a[currentCandSort.field];
            let valB = b[currentCandSort.field];
            if (typeof valA === 'string') { valA = valA.toLowerCase(); valB = valB.toLowerCase(); }
            if (valA < valB) return currentCandSort.dir === 'asc' ? -1 : 1;
            if (valA > valB) return currentCandSort.dir === 'asc' ? 1 : -1;
            return 0;
        });
    }

    const iconName = document.getElementById('sort-cand-name');
    const iconScore = document.getElementById('sort-cand-score');
    if(iconName) iconName.className = 'bi bi-arrow-down-up sort-icon';
    if(iconScore) iconScore.className = 'bi bi-arrow-down-up sort-icon';

    if (currentCandSort.dir && currentCandSort.field) {
        const activeIcon = currentCandSort.field === 'name' ? iconName : iconScore;
        activeIcon.className = currentCandSort.dir === 'asc'
            ? 'bi bi-sort-down-alt sort-icon active'
            : 'bi bi-sort-up sort-icon active';
    }

    let passed = 0, failed = 0;
    filtered.forEach(c => {
        if(c.score >= 60) passed++;
        else if(c.score > 0) failed++;
    });
    document.getElementById("candCountBadge").textContent = filtered.length;
    document.getElementById("candPassedCount").textContent = passed;
    document.getElementById("candFailedCount").textContent = failed;

    filtered.forEach(c => {
        const tr = document.createElement('tr');
        let statusHtml = "";
        if (!c.isRegistered) statusHtml = `<span class="badge badge-status-pending">Not Registered</span>`;
        else if (c.score >= 60) statusHtml = `<span class="badge badge-status-passed">Passed</span>`;
        else if (c.score > 0) statusHtml = `<span class="badge badge-status-failed">Failed</span>`;
        else statusHtml = `<span class="badge badge-status-pending">Pending</span>`;

        tr.innerHTML = `
            <td class="ps-4 fw-medium">${c.name}</td>
            <td class="text-secondary score-text">${c.score}</td> 
            <td>${statusHtml}</td>
            <td class="text-end pe-4">
                <button class="btn btn-icon btn-edit" onclick="openEditCandidateModal('${c.name}')">
                    <i class="bi bi-pencil-fill"></i>
                </button>
                <button class="btn btn-icon btn-delete" onclick="openDeleteCandidateModal('${c.name}')">
                    <i class="bi bi-trash-fill"></i>
                </button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function sortData(field) {
    if (currentSort.field === field) {
        if (currentSort.dir === 'asc') currentSort.dir = 'desc';
        else if (currentSort.dir === 'desc') currentSort.dir = null;
        else currentSort.dir = 'asc';
    } else {
        currentSort.field = field;
        currentSort.dir = 'asc';
    }
    renderQuestions(questionsData);
}

function updateSortIcons(state, fields) {
    fields.forEach(f => {
        const icon = document.getElementById(`sort-${f}`);
        if(icon) {
            icon.className = 'bi bi-arrow-down-up sort-icon';
            if(state.field === f && state.dir) {
                icon.className = state.dir === 'asc' ? 'bi bi-sort-down-alt sort-icon active' : 'bi bi-sort-up sort-icon active';
            }
        }
    });
}

async function handleAdd(e) {
    e.preventDefault();
    const text = document.getElementById("qText").value;
    const marks = parseInt(document.getElementById("qMarks").value);
    const category = document.getElementById("qCategory").value;
    if (marks > 100) return showError("Max points is 100");
    await fetch(API_URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({text, marks, category})
    });
    document.getElementById("addForm").reset();
    showSuccess("Question added!");
    loadQuestions();
}

function openEditModal(id) {
    const q = questionsData.find(x => x.id === id);
    if (!q) return;
    document.getElementById("editId").value = q.id;
    document.getElementById("editText").value = q.text;
    document.getElementById("editMarks").value = q.marks;
    document.getElementById("editCategory").value = q.category || 'General';
    editModal.show();
}

async function saveEdit() {
    const id = document.getElementById("editId").value;
    const text = document.getElementById("editText").value;
    const marks = parseInt(document.getElementById("editMarks").value);
    const category = document.getElementById("editCategory").value;
    if (marks > 100) return showError("Max points is 100");
    await fetch(API_URL, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({id: parseInt(id), text, marks, category})
    });
    editModal.hide();
    showSuccess("Updated!");
    loadQuestions();
}

function openDeleteModal(id) {
    const q = questionsData.find(x => x.id === id);
    if(q) document.getElementById("deletePreviewText").textContent = q.text;
    document.getElementById("deleteId").value = id;
    deleteModal.show();
}

async function confirmDelete() {
    const id = document.getElementById("deleteId").value;
    await fetch(`${API_URL}?id=${id}`, { method: 'DELETE' });
    deleteModal.hide();
    showSuccess("Deleted!");
    loadQuestions();
}

function sortCandidates(field) {
    if (currentCandSort.field === field) {
        if (currentCandSort.dir === 'asc') currentCandSort.dir = 'desc';
        else if (currentCandSort.dir === 'desc') currentCandSort.dir = null;
        else currentCandSort.dir = 'asc';
    } else {
        currentCandSort.field = field;
        currentCandSort.dir = 'asc';
    }
    renderCandidates(candidatesData);
}

async function handleAddCandidate(e) {
    e.preventDefault();
    const name = document.getElementById("cName").value;
    const age = parseInt(document.getElementById("cAge").value);
    const score = parseInt(document.getElementById("cScore").value) || 0;
    const isRegistered = document.getElementById("cRegistered").checked;
    if (score > 100) return showError("Max score is 100");
    await fetch(API_CANDIDATE_URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name, age, score, isRegistered})
    });
    document.getElementById("addCandidateForm").reset();
    document.getElementById("cRegistered").checked = true;
    showSuccess("Added!");
    loadCandidates();
}

function openEditCandidateModal(name) {
    const c = candidatesData.find(x => x.name === name);
    if (!c) return;
    document.getElementById("editCandName").value = c.name;
    document.getElementById("editCandAge").value = c.age;
    document.getElementById("editCandScore").value = c.score;
    document.getElementById("editCandRegistered").checked = c.isRegistered;
    editCandidateModal.show();
}

async function saveCandidateEdit() {
    const name = document.getElementById("editCandName").value;
    const age = parseInt(document.getElementById("editCandAge").value);
    const score = parseInt(document.getElementById("editCandScore").value);
    const isRegistered = document.getElementById("editCandRegistered").checked;
    if (score > 100) return showError("Max score is 100");
    await fetch(API_CANDIDATE_URL, {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({name, age, score, isRegistered})
    });
    editCandidateModal.hide();
    showSuccess("Updated!");
    loadCandidates();
}

function openDeleteCandidateModal(name) {
    document.getElementById("deleteCandName").value = name;
    document.getElementById("deleteCandPreviewText").textContent = name;
    deleteCandidateModal.show();
}

async function confirmDeleteCandidate() {
    const name = document.getElementById("deleteCandName").value;
    const encodedName = encodeURIComponent(name);
    await fetch(`${API_CANDIDATE_URL}?name=${encodedName}`, { method: 'DELETE' });
    deleteCandidateModal.hide();
    showSuccess("Deleted!");
    loadCandidates();
}

function exportToCSV() {
    if (!questionsData.length) return showError("No data to export");
    let csvContent = "data:text/csv;charset=utf-8,ID,Category,Question Text,Marks\n";
    questionsData.forEach(row => {
        let safeText = row.text ? row.text.replace(/"/g, '""') : "";
        csvContent += `${row.id},"${row.category}","${safeText}",${row.marks}\n`;
    });
    downloadCSV(csvContent, "questions_bank.csv");
}

function exportCandidatesToCSV() {
    if (!candidatesData.length) return showError("No data to export");
    let csvContent = "data:text/csv;charset=utf-8,Name,Age,Score,Status\n";
    candidatesData.forEach(row => {
        let status = row.isRegistered ? "Registered" : "Pending";
        csvContent += `"${row.name}",${row.age},${row.score},${status}\n`;
    });
    downloadCSV(csvContent, "candidates_list.csv");
}

function downloadCSV(content, fileName) {
    const encodedUri = encodeURI(content);
    const link = document.createElement("a");
    link.setAttribute("href", encodedUri);
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

function debounce(func, wait) {
    let timeout;
    return function(...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => func.apply(this, args), wait);
    };
}
function showSuccess(msg) {
    const el = document.getElementById("successMsg");
    if(el) el.textContent = msg;
    successToast.show();
}
function showError(msg) {
    const el = document.getElementById("errorMsg");
    if(el) el.textContent = msg;
    errorToast.show();
}

// =========================================
// === EXAM LOGIC (NEW FOR STEP 2)       ===
// =========================================

async function initExam() {
    // 1. Загружаем список кандидатов в Select
    try {
        const res = await fetch(API_CANDIDATE_URL);
        const candidates = await res.json();
        const select = document.getElementById("examCandidateSelect");

        select.innerHTML = '<option selected disabled>Choose your profile...</option>';
        candidates.forEach(c => {
            const opt = document.createElement("option");
            opt.value = c.name;
            opt.textContent = c.name;
            select.appendChild(opt);
        });
    } catch(e) { console.error(e); }
}

async function startExam() {
    const candidateName = document.getElementById("examCandidateSelect").value;
    if (candidateName === "Choose your profile...") return showError("Please select your name!");

    // Переключаем карточки
    document.getElementById("examLoginCard").classList.add("d-none");
    document.getElementById("examQuestionsCard").classList.remove("d-none");

    // Загружаем вопросы
    try {
        const res = await fetch("/exam/generate");
        const questions = await res.json();
        renderExamQuestions(questions);
    } catch(e) { showError("Failed to load exam"); }
}

function renderExamQuestions(questions) {
    const container = document.getElementById("examQuestionsContainer");
    container.innerHTML = "";

    questions.forEach((q, index) => {
        const div = document.createElement("div");
        div.className = "question-item";
        div.innerHTML = `
            <h6 class="fw-bold text-secondary mb-3">Question ${index + 1} <span class="badge bg-light text-dark border ms-2">${q.marks} pts</span></h6>
            <p class="fs-5 mb-3">${q.text}</p>
            <textarea class="form-control exam-answer" data-id="${q.id}" rows="3" placeholder="Type your answer here..."></textarea>
        `;
        container.appendChild(div);
    });
}

async function submitExam() {
    const candidateName = document.getElementById("examCandidateSelect").value;
    const inputs = document.querySelectorAll(".exam-answer");
    const answers = [];

    inputs.forEach(input => {
        answers.push({
            id: parseInt(input.dataset.id),
            answer: input.value
        });
    });

    try {
        const res = await fetch("/exam/submit", {
            method: "POST",
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ candidateName, answers })
        });
        const result = await res.json();
        showExamResult(result.score);
    } catch(e) { showError("Submission failed"); }
}

function showExamResult(score) {
    document.getElementById("examQuestionsCard").classList.add("d-none");
    const resCard = document.getElementById("examResultCard");
    resCard.classList.remove("d-none");

    // Анимация чисел
    const scoreEl = document.getElementById("resultScore");
    let current = 0;
    const timer = setInterval(() => {
        current += 1;
        scoreEl.textContent = current;
        if (current >= score) clearInterval(timer);
    }, 20);

    const msg = document.getElementById("resultMessage");
    const icon = document.getElementById("resultIcon");

    if (score >= 50) {
        msg.className = "alert alert-success d-inline-block px-5 fw-bold";
        msg.textContent = "PASSED";
        icon.innerHTML = '<i class="bi bi-emoji-smile-fill text-success" style="font-size: 5rem;"></i>';
    } else {
        msg.className = "alert alert-danger d-inline-block px-5 fw-bold";
        msg.textContent = "FAILED";
        icon.innerHTML = '<i class="bi bi-emoji-frown-fill text-danger" style="font-size: 5rem;"></i>';
    }
}