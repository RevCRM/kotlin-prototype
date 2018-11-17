import { GoogleSignIn } from './auth/GoogleSignIn';

const app = document.getElementById('app') as HTMLDivElement;

app.innerHTML = `
<h2>RevCRM</h2>
<button id="btn">Test Login</button>

<div class="g-signin2" data-onsuccess="onSignIn"></div>
`;

function testLogin() {
    alert('logging in...');
}

const button = document.getElementById('btn') as HTMLButtonElement;
button.onclick = () => testLogin();

const auth = new GoogleSignIn();
auth.initialise();
