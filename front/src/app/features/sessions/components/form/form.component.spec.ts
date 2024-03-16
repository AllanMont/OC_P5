import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { Session } from '../../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const mockSessions: Session[] = [
      { id: 1, name: 'Session 1', date: new Date(), teacher_id: 1, description: 'Description 1', users: []},
      { id: 2, name: 'Session 2', date: new Date(), teacher_id: 2, description: 'Description 2', users: []}
    ];

    service.all().subscribe(sessions => {
      expect(sessions.length).toBe(2);
      expect(sessions).toEqual(mockSessions);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should retrieve session by id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      date: new Date(),
      teacher_id: 1,
      description: 'Description 1',
      users: []
    };

    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should delete session by id', () => {
    const sessionId = '1';

    service.delete(sessionId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should create a new session', () => {
    const newSession: Session = {
      id: 1,
      name: 'New Session',
      date: new Date(),
      teacher_id: 1,
      description: 'New Description',
      users: []
    };

    service.create(newSession).subscribe(session => {
      expect(session).toEqual(newSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(newSession);
  });

  it('should update an existing session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Session',
      date: new Date(),
      teacher_id: 1,
      description: 'Updated Description',
      users: []
    };

    service.update('1', updatedSession).subscribe(session => {
      expect(session).toEqual(updatedSession);
    });

    const req = httpMock.expectOne(`api/session/${1}`);
    expect(req.request.method).toBe('PUT');
    req.flush(updatedSession);
  });

  it('should allow a user to participate in a session', () => {
    const sessionId = '1';
    const userId = 'user123';

    service.participate(sessionId, userId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should allow a user to unparticipate in a session', () => {
    const sessionId = '1';
    const userId = 'user123';

    service.unParticipate(sessionId, userId).subscribe();

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
