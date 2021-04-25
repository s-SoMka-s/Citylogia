using Microsoft.EntityFrameworkCore.Migrations;

namespace Core.Db.Migrations
{
    public partial class Addshortdescrtoplace : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "ShortDescription",
                schema: "citylogia",
                table: "Places",
                type: "text",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "ShortDescription",
                schema: "citylogia",
                table: "Places");
        }
    }
}
