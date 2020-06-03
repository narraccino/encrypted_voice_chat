clear;
clc;

algorithm= 'AES';

for i= 1:3

    
    if i==1
        str='128'; 
    elseif i==2
        str='192'; 
    else
        str='256'; 
    end
    
strBitA = strcat(algorithm,'-',str,'-8000.xls');
strBitB = strcat(algorithm,'-',str,'-11025.xls');
strBitC = strcat(algorithm,'-',str,'-16000.xls');
strBitD = strcat(algorithm,'-',str,'-22050.xls');
strBitE = strcat(algorithm,'-',str,'-44100.xls');
    
TA = table2array(readtable(strBitA));
TB = table2array(readtable(strBitB));
TC = table2array(readtable(strBitC));
TD = table2array(readtable(strBitD));
TE = table2array(readtable(strBitE));

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

A= Atemp(round(rowsA-((2/3)*rowsA)):rowsA);
B= Btemp(round(rowsB-((2/3)*rowsB)):rowsB);
C= Ctemp(round(rowsC-((2/3)*rowsC)):rowsC);
D= Dtemp(round(rowsD-((2/3)*rowsD)):rowsD);
E= Etemp(round(rowsE-((2/3)*rowsE)):rowsE);

% B= Btemp(rowsB-10:rowsB);
% C= Ctemp(rowsC-10:rowsC);
% D= Dtemp(rowsD-10:rowsD);
% E= Etemp(rowsE-10:rowsE);


Aval= mean(A);
Bval= mean(B);
Cval= mean(C);
Dval= mean(D);
Eval= mean(E);

vettSample=[8000 11025 16000 22050 44100];

if i==1
    vettmillisec128= [Aval, Bval, Cval, Dval, Eval];
    elseif i==2
    vettmillisec192= [Aval, Bval, Cval, Dval, Eval];
    else
    vettmillisec256= [Aval, Bval, Cval, Dval, Eval];
end
    

end



%Salvo in una immagine il grafico
h= figure;

plot(vettSample,vettmillisec128, 'DisplayName', strcat(algorithm,'-128'));
hold on
plot(vettSample,vettmillisec192, 'DisplayName', strcat(algorithm,'-192'));
plot(vettSample,vettmillisec256, 'DisplayName', strcat(algorithm,'-256'));
title(algorithm);

xlabel('Payload (MB)')
ylabel('Nanoseconds (ns)')
xticks([8000 11025 16000 22050 44100])
xticklabels({'7.8','10.7','15.6','21.5','43.0'})
legend('show')
saveas(h,algorithm,'jpg');
hold off


