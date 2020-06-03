clear;
clc;


TA = table2array(readtable("AES-128-8000.xls"));
TB = table2array(readtable("AES-128-11025.xls"));
TC = table2array(readtable("AES-128-16000.xls"));
TD = table2array(readtable("AES-128-22050.xls"));
TE = table2array(readtable("AES-128-44100.xls"));

szA= size(TA);
rowsA= szA(1);

szB = size(TB);
rowsB= szB(1);

szC= size(TC);
rowsC= szC(1);

szD = size(TD);
rowsD= szD(1);

szE= size(TE);
rowsE= szE(1);


Atemp = str2double(TA);
Btemp = str2double(TB);
Ctemp = str2double(TC);
Dtemp = str2double(TD);
Etemp = str2double(TE);

A= Atemp(rowsA-10:rowsA);
B= Btemp(rowsB-10:rowsB);
C= Ctemp(rowsC-10:rowsC);
D= Dtemp(rowsD-10:rowsD);
E= Etemp(rowsE-10:rowsE);


Aval= mean(A)/100;
Bval= mean(B)/100;
Cval= mean(C)/100;
Dval= mean(D)/100;
Eval= mean(E)/100;

vettmillisec= [Aval, Bval, Cval, Dval, Eval];
vettSample=[1, 2,3,4,5];

plot(vettSample,vettmillisec )











% numPack= B(9,1);
% [x,y] = find(A==numPack);
% timeSend= A(x,y+1);
% arrayTemp= timeSend;
% for i=9:rowsB
% format long 
% numPack= B(i,1);
% [x,y] = find(A==numPack);
% timeSend= A(x,y+1);
% arrayTemp= [arrayTemp;timeSend];
% end
% 
% format long
% B1=B(9:rowsB,1:2);
% C=[B1,arrayTemp];
% 
% szC = size(C);
% 
% invio =C(1,3);
% ricezione= C(1,2);
% tempo= ricezione-invio;
%  
% for i=2:szC
%    invio =C(i,3);
%    ricezione= C(i,2);
%    diff = ricezione-invio;
%    tempo= [tempo;diff];
%    
% end
% 
% rowsC= szC(1);
% 
% TimeUDP= mean(tempo(3:rowsC,1))
