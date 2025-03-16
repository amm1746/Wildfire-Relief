import { Need } from './need';

describe('Need', () => {
  it('should create an instance', () => {
    const need = new Need('Test Need', 10.99, 5, 'Test Type');
    expect(need).toBeTruthy();
  });

  it('should have the correct properties', () => {
    const need = new Need('Test Need', 10.99, 5, 'Test Type');

    expect(need.name).toBe('Test Need');
    expect(need.cost).toBe(10.99);
    expect(need.quantity).toBe(5);
    expect(need.type).toBe('Test Type');
  });
});