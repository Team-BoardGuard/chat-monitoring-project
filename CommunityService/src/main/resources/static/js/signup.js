// 닉네임 입력 필드와 메시지 표시 영역을 가져옵니다.
const nicknameInput = document.getElementById('nickname');
const nicknameCheckMsg = document.getElementById('nickname-check-msg');
let isNicknameAvailable = false; // 닉네임 사용 가능 여부를 추적하는 변수

// 닉네임 입력이 끝났을 때(focusout) 중복 확인을 실행합니다.
nicknameInput.addEventListener('focusout', async function() {
    const nickname = nicknameInput.value;

    if (nickname.length === 0) {
        nicknameCheckMsg.textContent = '';
        return;
    }

    try {
        // 백엔드 API에 GET 요청을 보내 닉네임 중복 여부를 확인합니다.
        const response = await fetch(`/api/user/check-nickname?nickname=${nickname}`);
        const data = await response.json();
        
        if (data.isAvailable) {
            nicknameCheckMsg.textContent = '사용 가능한 닉네임입니다.';
            nicknameCheckMsg.className = 'available';
            isNicknameAvailable = true;
        } else {
            nicknameCheckMsg.textContent = '이미 사용 중인 닉네임입니다.';
            nicknameCheckMsg.className = 'unavailable';
            isNicknameAvailable = false;
        }
    } catch (error) {
        console.error('Nickname check failed:', error);
        nicknameCheckMsg.textContent = '닉네임 확인 중 오류가 발생했습니다.';
        nicknameCheckMsg.className = 'unavailable';
        isNicknameAvailable = false;
    }
});

// 회원가입 폼 제출 시, 닉네임 사용 가능 여부를 한번 더 확인합니다.
const signupForm = document.getElementById('signupForm');
signupForm.addEventListener('submit', function(event) {
    if (!isNicknameAvailable) {
        alert('사용할 수 없는 닉네임입니다. 다른 닉네임을 입력해주세요.');
        event.preventDefault(); // 폼 제출을 막습니다.
    }
});