import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { helperAuthGuard } from './helper-auth.guard';

describe('helperAuthGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => helperAuthGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
