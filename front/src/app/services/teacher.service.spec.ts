import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { Teacher } from '../interfaces/teacher.interface';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;

  const mockTeachers : Teacher[] = [
    {
      id: 1,
      lastName: 'Doe',
      firstName: 'John',
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      lastName: 'Doe',
      firstName: 'Jane',
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(TeacherService);
  });

  it('should get All teachers', () => {
    service.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
    });
  });

  it('should get a teacher by id', () => {
    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeachers[0]);
    });
  });
  
});
