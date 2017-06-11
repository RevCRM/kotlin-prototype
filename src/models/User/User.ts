
import * as rev from 'rev-models';
import { UserMeta } from './UserMeta';
import { ModelRegistry } from 'rev-models';
import { ModelOperationResult } from 'rev-models/lib/operations/operationresult';

// Stored User Model

export class User extends UserMeta {
    _registry: ModelRegistry;

    @rev.PasswordField()
        password: string;

    async login() {
        const result = new ModelOperationResult({ operation: 'login' });
        if (!this.username || !this.password) {
            result.addError('Username / password not found');
        }
        else {
            const matches = await this._registry.read(User, {
                username: this.username,
                password: this.password,
                active: true
            });
            if (matches.results.length == 1) {
                result.addError('Username / password not found');
            }
            else {
                console.log('logged in!');
            }
        }
        return result;
    }
}
