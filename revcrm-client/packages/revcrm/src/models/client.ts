
import { UserLoginFormModel } from './User/views/UserLoginForm';
import { RevCRMClient } from '../client';

export function registerModels(client: RevCRMClient) {
    client.models.register(UserLoginFormModel);
}
