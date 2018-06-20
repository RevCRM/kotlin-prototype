const bcrypt = require('bcrypt');

const pwToHash = process.argv[2];
console.log('Plaintext:', pwToHash);

(async () => {
    const hash = await bcrypt.hash(pwToHash, 10);
    console.log('Bcrypt Hash:', hash);
})();
