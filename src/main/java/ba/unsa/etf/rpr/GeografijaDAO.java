package ba.unsa.etf.rpr;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;


public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection connection;
    private PreparedStatement statement;
    private ArrayList<Grad> gradovi = new ArrayList<>();

    public Connection getConnection () {
        return connection;
    }

    public ArrayList<Drzava> getDrzave() {
        return drzave;
    }

    public void setDrzave(ArrayList<Drzava> drzave) {
        this.drzave = drzave;
    }

    private ArrayList<Drzava> drzave = new ArrayList<>();

    private static void initialize() {
        instance = new GeografijaDAO();
    }

    public static GeografijaDAO getInstance() {
        if (instance == null) initialize();
        return instance;
    }

    private GeografijaDAO() {
        File db = new File("baza.db");
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
            /*prvi zadatak
                String url = "jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB";
                connection = DriverManager.getConnection(url, "MK18290", "r2Cjv03n");
            */
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS  grad (id integer primary key, naziv text, broj_stanovnika integer, drzava integer references drzava)");
            statement.executeUpdate();
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS  drzava (id integer primary key, naziv text, glavni_grad integer references grad)");
            statement.executeUpdate();
            popunjavanjeTabela();
            statement = connection.prepareStatement("DELETE FROM grad");
            statement.executeUpdate();
            statement = connection.prepareStatement("DELETE FROM drzava");
            statement.executeUpdate();
            statement = connection.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, NULL)");
            for (Grad grad : gradovi) {
                statement.setInt(1, grad.getId());
                statement.setString(2, grad.getNaziv());
                statement.setInt(3, grad.getBrojStanovnika());
                statement.executeUpdate();
            }
            statement = connection.prepareStatement("INSERT  INTO drzava VALUES(?, ?, ?)");
            for (Drzava drzava : drzave) {
                statement.setInt(1, drzava.getId());
                statement.setString(2, drzava.getNaziv());
                statement.setInt(3, drzava.getGlavniGrad().getId());
                statement.executeUpdate();
            }
            statement = connection.prepareStatement("UPDATE grad SET drzava = ? WHERE id = ?");
            for (Grad grad : gradovi) {
                statement.setInt(1, grad.getDrzava().getId());
                statement.setInt(2, grad.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void popunjavanjeTabela() {
        Grad p = new Grad(1, "Pariz", 2206488, null);
        Grad l = new Grad(2, "London", 8825000, null);
        Grad m = new Grad(4, "Manchester", 545500, null);
        Grad g = new Grad(5, "Graz", 280200, null);
        Grad b = new Grad(3, "Beč", 1899055, null);

        Drzava engleska = new Drzava(2, "Velika Britanija", l);
        Drzava austrija = new Drzava(3, "Austrija", b);
        Drzava francuska = new Drzava(1, "Francuska", p);

        drzave.add(engleska);
        drzave.add(austrija);
        drzave.add(francuska);
        p.setDrzava(francuska);
        l.setDrzava(engleska);
        b.setDrzava(austrija);
        m.setDrzava(engleska);
        g.setDrzava(austrija);
        gradovi.add(p);
        gradovi.add(l);
        gradovi.add(b);
        gradovi.add(m);
        gradovi.add(g);
    }

    public Grad glavniGrad(String drzava) {
        try {
            statement = connection.prepareStatement("SELECT g.id, g.naziv, g.broj_stanovnika, d.id, d.naziv FROM grad g, drzava d WHERE d.glavni_grad = g.id AND d.naziv = ?");
            statement.setString(1, drzava);
            ResultSet rs = statement.executeQuery();
            Grad grad = new Grad();
            Drzava d = new Drzava();
            grad.setDrzava(d);
            d.setGlavniGrad(grad);
            boolean nalaziSe = false;
            while (rs.next()) {
                nalaziSe = true;
                int idGrada = rs.getInt(1);
                String nazivGrada = rs.getString(2);
                int brojStanovnika = rs.getInt(3);
                int idDrzave = rs.getInt(4);
                String nazivDrzave = rs.getString(5);
                grad.setId(idGrada);
                grad.setNaziv(nazivGrada);
                grad.setBrojStanovnika(brojStanovnika);
                d.setId(idDrzave);
                d.setNaziv(nazivDrzave);
            }
            if (nalaziSe) {
                return grad;
            }
        } catch (SQLException e) {

        }
        return null;
    }

    public void obrisiDrzavu(String drzava) {
        try {
            statement = connection.prepareStatement("SELECT g.id FROM grad g, drzava d WHERE g.drzava = d.id AND d.naziv = ?");
            statement.setString(1, drzava);
            ResultSet result = statement.executeQuery();
            int brojac = 0;
            while (result.next()) {
                int idGrad = result.getInt(1);
                PreparedStatement podUpit = connection.prepareStatement("DELETE FROM grad WHERE id = ?");
                podUpit.setInt(1, idGrad);
                podUpit.executeUpdate();
                brojac++;
            }
            if (brojac == 0)
                return;
            statement = connection.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
            statement.setString(1, drzava);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        try {
            statement = connection.prepareStatement("SELECT * FROM grad");
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Grad grad = new Grad();
                Drzava d = new Drzava();
                int idGrada = result.getInt(1);
                String nazivGrada = result.getString(2);
                int brojStanovnika = result.getInt(3);
                int idDrzave = result.getInt(4);
                grad.setId(idGrada);
                grad.setNaziv(nazivGrada);
                grad.setBrojStanovnika(brojStanovnika);
                d.setId(idDrzave);
                grad.setDrzava(d);
                gradovi.add(grad);
            }
            statement = connection.prepareStatement("SELECT * FROM drzava");
            result = statement.executeQuery();

            while (result.next()) {
                Drzava d = new Drzava();
                int idDrzave = result.getInt(1);
                String nazivDrzave = result.getString(2);
                d.setId(idDrzave);
                d.setNaziv(nazivDrzave);
                int idGlavnogGrada = result.getInt(3);
                for (Grad grad : gradovi) {
                    if (grad.getDrzava().getId() == d.getId()) {
                        grad.setDrzava(d);
                    }
                    if (idGlavnogGrada == grad.getId()) {
                        d.setGlavniGrad(grad);
                    }
                }
                drzave.add(d);
            }
        } catch (SQLException e) {
            return null;
        }

        gradovi.sort(new Comparator<Grad>() {
            @Override
            public int compare(Grad o1, Grad o2) {
                Integer a=o2.getBrojStanovnika();
                Integer b=o1.getBrojStanovnika();
                return a.compareTo(b);
            }
        });
        return gradovi;

    }

    public void dodajGrad(Grad grad) {
        try {
            statement = connection.prepareStatement("SELECT id FROM grad WHERE naziv = ? AND broj_stanovnika IS NULL");
            statement.setString(1, grad.getNaziv());
            ResultSet result = statement.executeQuery();
            int id = -1;
            while (result.next())
                id = result.getInt(1);
            if (id != -1) {
                grad.setId(id);
                statement = connection.prepareStatement("SELECT id FROM drzava WHERE glavni_grad = ?");
                statement.setInt(1, id);
                result = statement.executeQuery();
                id = -1;
                while (result.next()) {
                    id = result.getInt(1);
                }
                Drzava temp = new Drzava();
                temp.setId(id);
                grad.setDrzava(temp);
                izmijeniGrad(grad);
                return;
            }
            statement = connection.prepareStatement("SELECT id FROM drzava WHERE naziv = ?");
            statement.setString(1, grad.getDrzava().getNaziv());
            result = statement.executeQuery();
            boolean imaDrzave = false;
            int idDrzave = 0;
            while (result.next()) {
                idDrzave = result.getInt(1);
                imaDrzave = true;
            }
            statement = connection.prepareStatement("SELECT id FROM grad ORDER BY id DESC");
            result = statement.executeQuery();
            int idGrada = 0;
            while (result.next()) {
                result.getInt(1);
                idGrada++;
            }
            idGrada++;
            statement = connection.prepareStatement("INSERT INTO grad VALUES (?, ?, ?, ?)");
            statement.setInt(1, idGrada);
            statement.setString(2, grad.getNaziv());
            statement.setInt(3, grad.getBrojStanovnika());
            if (!imaDrzave) {
                statement.setNull(4, Types.INTEGER);
            } else {
                statement.setInt(4, idDrzave);
            }
            statement.executeUpdate();
            if (!imaDrzave) {
                statement = connection.prepareStatement("SELECT id FROM drzava ORDER BY id DESC");
                result = statement.executeQuery();
                idDrzave = 0;
                while (result.next()) {
                    result.getInt(1);
                    idDrzave++;
                }
                idDrzave++;
                try {
                    statement = connection.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
                    statement.setInt(1, idDrzave);
                    statement.setString(2, grad.getDrzava().getNaziv());
                    statement.setInt(3, idGrada);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            statement = connection.prepareStatement("SELECT id FROM grad WHERE naziv = ?");
            statement.setString(1, drzava.getGlavniGrad().getNaziv());
            ResultSet result = statement.executeQuery();
            boolean imaGlGrada = false;
            int idGrada = 0;
            while (result.next()) {
                idGrada = result.getInt(1);
                imaGlGrada = true;
            }
            statement = connection.prepareStatement("SELECT id FROM drzava ORDER BY id DESC");
            result = statement.executeQuery();
            int idDrzave = 0;
            while (result.next()) {
                result.getInt(1);
                idDrzave++;
            }
            idDrzave++;
            statement = connection.prepareStatement("INSERT INTO drzava VALUES (?, ?, ?)");
            statement.setInt(1, idDrzave);
            statement.setString(2, drzava.getNaziv());
            if (!imaGlGrada)
                statement.setNull(3, Types.INTEGER);
            else
                statement.setInt(3, idGrada);
            statement.executeUpdate();

            if (!imaGlGrada) {
                statement = connection.prepareStatement("SELECT id FROM grad ORDER BY id DESC");
                result = statement.executeQuery();
                idGrada = 0;
                while (result.next()) {
                    result.getInt(1);
                    idGrada++;
                }
                idGrada++;
                statement = connection.prepareStatement("INSERT INTO grad VALUES (?, ?, NULL, NULL)");
                statement.setInt(1, idGrada);
                statement.setString(2, drzava.getGlavniGrad().getNaziv());
                statement.executeUpdate();
                statement = connection.prepareStatement("UPDATE drzava SET glavni_grad = ? WHERE id = ?");
                statement.setInt(1, idGrada);
                statement.setInt(2, idDrzave);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try {
            statement = connection.prepareStatement("UPDATE grad SET naziv = ?, broj_stanovnika = ?, drzava = ? WHERE id = ?");
            statement.setString(1, grad.getNaziv());
            statement.setInt(2, grad.getBrojStanovnika());
            statement.setInt(3, grad.getDrzava().getId());
            statement.setInt(4, grad.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String drzava) {
        Drzava d = new Drzava();
        try {
            statement = connection.prepareStatement("SELECT d.id, d.naziv, g.id, g.naziv, g.broj_stanovnika FROM drzava d, grad g WHERE d.glavni_grad = g.id AND d.naziv = ?");
            statement.setString(1, drzava);
            ResultSet result = statement.executeQuery();
            Grad glavniGrad = new Grad();
            d.setGlavniGrad(glavniGrad);
            glavniGrad.setDrzava(d);
            while (result.next()) {
                int idDrzava = result.getInt(1);
                d.setId(idDrzava);
                String nazivDrzave = result.getString(2);
                d.setNaziv(nazivDrzave);
                int idGrad = result.getInt(3);
                glavniGrad.setId(idGrad);
                String nazivGrad = result.getString(4);
                glavniGrad.setNaziv(nazivGrad);
                int brojStanovnika = result.getInt(5);
                glavniGrad.setBrojStanovnika(brojStanovnika);
            }
        } catch (SQLException ignored) {
            System.out.println("Drzava ne postoji");
            return null;
        }
        return d;

    }

    public static void removeInstance() {
        /*if(instance!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/
        instance = null;

    }

}





