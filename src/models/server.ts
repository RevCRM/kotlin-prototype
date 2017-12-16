
import { ModelManager, InMemoryBackend } from 'rev-models';
import { ModelApiManager } from 'rev-api';
import { User } from './User/User';
import { UserAuthData } from './User/backend/UserAuthData';
import { UserLoginFormModel } from './User/backend/UserLoginFormModel';
import { Company } from './Company/Company';
import { SelectionList } from './SelectionList/SelectionList';
import { Perspective } from './UI/Perspective';
import { View } from './UI/View';
import { PerspectiveView } from './UI/PerspectiveView';

export const serverModels = new ModelManager();
serverModels.registerBackend('default', new InMemoryBackend());
serverModels.register(User);
serverModels.register(UserAuthData);
serverModels.register(UserLoginFormModel);
serverModels.register(Perspective);
serverModels.register(View);
serverModels.register(PerspectiveView);
serverModels.register(Company);
serverModels.register(SelectionList);

export const api = new ModelApiManager(serverModels);
api.register(UserLoginFormModel);
api.register(Company);
api.register(SelectionList);
