clear;
clc;

str='256';

for i= 1:2

 switch i
    case 1
        algo= 'AES';
    case 2
        algo= 'RC6';   
     otherwise
end

      
    
strBitA = strcat(algo,'-',str,'-8000.xls');
strBitB = strcat(algo,'-',str,'-11025.xls');
strBitC = strcat(algo,'-',str,'-16000.xls');
strBitD = strcat(algo,'-',str,'-22050.xls');
strBitE = strcat(algo,'-',str,'-44100.xls');
    
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


Aval= mean(A);
Bval= mean(B);
Cval= mean(C);
Dval= mean(D);
Eval= mean(E);

vettSample=[8000 11025 16000 22050 44100];

if i==1
    vettmilliseca= [Aval, Bval, Cval, Dval, Eval];
    elseif i==2
    vettmillisecb= [Aval, Bval, Cval, Dval, Eval];
end
    

end



%Salvo in una immagine il grafico
h= figure;


plot(vettSample,vettmilliseca, 'DisplayName', 'AES');
hold on
plot(vettSample,vettmillisecb, 'DisplayName', 'RC6');

title(strcat('KEY ',' ',str,' ' ,' bit'));

xlabel('Payload (MB)')
ylabel('Nanoseconds (ns)')
xticks([8000 11025 16000 22050 44100])
xticklabels({'7.8','10.7','15.6','21.5','43.0'})
legend('show')
saveas(h,strcat('KEY_',str,'_bit'),'jpg');
hold off
